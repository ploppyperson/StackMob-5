package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@CommandMetadata(command = "remove", playerReq = false, desc = "Remove entities.")
public class Remove extends SubCommand {

    private final StackMob sm;
    public Remove(StackMob sm) {
        super(CommandArgument.construct(StringArgumentType.greedyString(), false, Arrays.asList("chunk", "world", "all")),
                CommandArgument.construct(StringArgumentType.greedyString(), true, Arrays.asList("animals", "hostile")));
        this.sm = sm;
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

}
