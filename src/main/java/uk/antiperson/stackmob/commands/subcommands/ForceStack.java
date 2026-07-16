package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.*;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@CommandMetadata(command = "forcestack", playerReq = false, desc = "Force all currently loaded entities to stack.")
public class ForceStack extends SubCommand {

    private final StackMob sm;
    public ForceStack(StackMob sm) {
        super();
        this.sm = sm;
    }

    @Override
    public void construct() {
        addCommandArgument(CommandArgument.construct(new TypeSpecifierArgument(), true, "type", "type of entities"));
    }

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        int count = 0;
        Predicate<LivingEntity> predicate = null;
        switch (ctx.getArgument("type", TypeSpecifier.class)) {
            case NAMED:
                predicate = pEntity -> pEntity.getCustomName() != null;
                break;
            case TAMED:
                predicate = pEntity -> (pEntity instanceof Tameable) && ((Tameable) pEntity).isTamed();
                break;
            case CHUNK:
                if (!(sender.getSender() instanceof Player)) {
                    sender.sendError("You need to be a player!");
                    return 2;
                }
                predicate = pEntity -> pEntity.getLocation().getChunk() == ((Player) sender.getSender()).getLocation().getChunk();
                break;
        }
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getEntitiesByClass(Mob.class)) {
                if (sm.getEntityManager().isStackedEntity(entity)) {
                    continue;
                }
                if (predicate != null && !predicate.test(entity)) {
                    continue;
                }
                if (sm.getMainConfig().getConfig(entity).isEntityBlacklisted(entity)) {
                    continue;
                }
                sm.getEntityManager().registerStackedEntity(entity).setSize(1);
                count++;
            }
        }
        String entityType = predicate != null ? ctx.getArgument("type", TypeSpecifier.class).toString().toLowerCase() + " " : "";
        sender.sendSuccess(count + " " + entityType + "entities have been forced to stack!");
        return Command.SINGLE_SUCCESS;
    }

    enum TypeSpecifier {
        NAMED,
        TAMED,
        CHUNK
    }

    public static class TypeSpecifierArgument implements CustomArgumentType.Converted<TypeSpecifier, String> {

        private static final DynamicCommandExceptionType ERROR_INVALID_FLAVOR = new DynamicCommandExceptionType(flavor -> {
            return MessageComponentSerializer.message().serialize(Component.text(flavor + " is not a type specifier"));
        });

        @Override
        public TypeSpecifier convert(String nativeType) throws CommandSyntaxException {
            try {
                return TypeSpecifier.valueOf(nativeType.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                throw ERROR_INVALID_FLAVOR.create(nativeType);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (TypeSpecifier flavor : TypeSpecifier.values()) {
                String name = flavor.toString();

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
