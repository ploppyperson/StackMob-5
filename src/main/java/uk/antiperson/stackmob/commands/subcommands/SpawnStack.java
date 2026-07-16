package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.*;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@CommandMetadata(command = "spawn", playerReq = true, desc = "Spawn a new stack.")
public class SpawnStack extends SubCommand {

    private final StackMob sm;
    private final HashSet<UUID> confirm;
    public SpawnStack(StackMob sm) {
        super();
        this.sm = sm;
        this.confirm = new HashSet<>();
    }

    @Override
    public void construct() {
        addCommandArgument(CommandArgument.construct(new EntityTypeArgument(), false, "type", "entity type"));
        addCommandArgument(CommandArgument.construct(IntegerArgumentType.integer(1, Integer.MAX_VALUE), false,  "size","stack size"));
        addCommandArgument(CommandArgument.construct(StringArgumentType.word(), true,  "amount","number of stacks"));
    }

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        Player player = (Player) sender.getSender();
        EntityType entityType = ctx.getArgument("type", EntityType.class);
        int newSize = ctx.getArgument("size", Integer.class);
        if (newSize < 1) {
            sender.sendError("You cannot spawn a stack with size less than one!");
            return Command.SINGLE_SUCCESS;
        }
        int maxSize = sm.getMainConfig().getConfig(entityType).getMaxStack();
        if (newSize > maxSize) {
            sender.sendError("Provided stack value is too large! (the maximum for " + entityType + " is " + maxSize + ")");
            return Command.SINGLE_SUCCESS;
        }
        int amountOfStacks = ctx.getArgument("amount", Integer.class);
        if (amountOfStacks < 1) {
            sender.sendError("You cannot spawn less than one stack!");
            return Command.SINGLE_SUCCESS;
        }
        if (amountOfStacks > 20 && !confirm.contains(((Player) sender.getSender()).getUniqueId())) {
            sender.sendInfo("Are you sure you want to spawn " + amountOfStacks + " stacks?");
            sender.sendInfo("Run the same command again to confirm.");
            confirm.add(((Player) sender.getSender()).getUniqueId());
            return Command.SINGLE_SUCCESS;
        }
        for (int i = 0; i < amountOfStacks; i++) {
            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), entityType);
            StackEntity stackEntity = sm.getEntityManager().registerStackedEntity(entity);
            stackEntity.setSize(newSize);
        }
        String stackString = amountOfStacks == 1 ? "A new stack has" : amountOfStacks + " stacks have";
        sender.sendSuccess(stackString + " been spawned.");
        confirm.remove(((Player) sender.getSender()).getUniqueId());
        return Command.SINGLE_SUCCESS;
    }

    public static class EntityTypeArgument implements CustomArgumentType.Converted<EntityType, String> {

        private static final DynamicCommandExceptionType ERROR_INVALID_FLAVOR = new DynamicCommandExceptionType(flavor -> {
            return MessageComponentSerializer.message().serialize(Component.text(flavor + " is not an entity type"));
        });

        @Override
        public EntityType convert(String nativeType) throws CommandSyntaxException {
            try {
                EntityType entityType = EntityType.valueOf(nativeType.toUpperCase(Locale.ROOT));
                if (entityType.getEntityClass() == null) throw ERROR_INVALID_FLAVOR.create(nativeType);
                if (entityType.getEntityClass().isAssignableFrom(Mob.class)) {
                    throw ERROR_INVALID_FLAVOR.create(nativeType);
                }
                return entityType;
            } catch (IllegalArgumentException ignored) {
                throw ERROR_INVALID_FLAVOR.create(nativeType);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (EntityType flavor : EntityType.values()) {
                String name = flavor.toString();

                if (flavor.getEntityClass() == null) continue;
                if (flavor.getEntityClass().isAssignableFrom(Mob.class)) {
                    continue;
                }

                // Only suggest if the flavor name matches the user input
                if (name.startsWith(builder.getRemainingLowerCase())) {
                    builder.suggest(flavor.toString());
                }
            }

            return builder.buildFuture();
        }

        @Override
        public ArgumentType<String> getNativeType() {
            return StringArgumentType.word();
        }
    }
}
