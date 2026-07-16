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
import org.bukkit.Chunk;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.*;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@CommandMetadata(command = "remove", playerReq = false, desc = "Remove entities.")
public class Remove extends SubCommand {

    private final StackMob sm;
    public Remove(StackMob sm) {
        super();
        this.sm = sm;
    }

    @Override
    public void construct() {
        addCommandArgument(CommandArgument.construct(new LocationSpecifierArgument(), false, "area", "area of operation"));
        addCommandArgument(CommandArgument.construct(new EntitySpecifierArgument(), true, "type", "entity group"));
    }

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        String type = ctx.getArgument("type", String.class);
        String area = ctx.getArgument("area", String.class);
        Function<Entity, Boolean> function = entity -> entity instanceof Mob;
        if (type.length() > 0) {
            switch (type) {
                case "animals":
                    function = entity -> entity instanceof Animals;
                    break;
                case "hostile":
                    function = entity -> entity instanceof Monster;
                    break;
            }
        }
        Set<Chunk> chunks = new HashSet<>();
        switch (area) {
            case "chunk":
                if (!(sender.getSender() instanceof Player)) {
                    sender.sendError("You need to be a player!");
                    return Command.SINGLE_SUCCESS;
                }
                chunks.add(((Player) sender.getSender()).getLocation().getChunk());
                break;
            case "world":
                if (!(sender.getSender() instanceof Player)) {
                    sender.sendError("You need to be a player!");
                    return Command.SINGLE_SUCCESS;
                }
                chunks.addAll(List.of(((Player) sender.getSender()).getWorld().getLoadedChunks()));
                break;
            case "all":
                Bukkit.getWorlds().forEach(world -> chunks.addAll(List.of(world.getLoadedChunks())));
                break;
        }
        for (Chunk chunk : chunks) {
            for (Entity entity : chunk.getEntities()) {
                if (!function.apply(entity)) {
                    continue;
                }
                StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) entity);
                if (stackEntity == null) {
                    continue;
                }
                stackEntity.remove();
            }
        }
        sender.sendSuccess(Utilities.capitalizeString(area) + " entities matching your criteria have been removed.");
        return Command.SINGLE_SUCCESS;
    }

    public enum LocationSpecifier {
        CHUNK,
        WORLD,
        ALL
    }

    public enum EntitySpecifier {
        ANIMALS,
        HOSTILE
    }

    public static class LocationSpecifierArgument implements CustomArgumentType.Converted<LocationSpecifier, String> {

        private static final DynamicCommandExceptionType ERROR_INVALID_FLAVOR = new DynamicCommandExceptionType(flavor -> {
            return MessageComponentSerializer.message().serialize(Component.text(flavor + " is not a location specifier"));
        });

        @Override
        public LocationSpecifier convert(String nativeType) throws CommandSyntaxException {
            try {
                return LocationSpecifier.valueOf(nativeType.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                throw ERROR_INVALID_FLAVOR.create(nativeType);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (LocationSpecifier flavor : LocationSpecifier.values()) {
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

    public static class EntitySpecifierArgument implements CustomArgumentType.Converted<EntitySpecifier, String> {

        private static final DynamicCommandExceptionType ERROR_INVALID_FLAVOR = new DynamicCommandExceptionType(flavor -> {
            return MessageComponentSerializer.message().serialize(Component.text(flavor + " is not an entity specifier"));
        });

        @Override
        public EntitySpecifier convert(String nativeType) throws CommandSyntaxException {
            try {
                return EntitySpecifier.valueOf(nativeType.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                throw ERROR_INVALID_FLAVOR.create(nativeType);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (EntitySpecifier flavor : EntitySpecifier.values()) {
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
