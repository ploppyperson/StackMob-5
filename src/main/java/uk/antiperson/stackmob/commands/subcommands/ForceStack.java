package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
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
import java.util.function.Predicate;

@CommandMetadata(command = "forcestack", playerReq = false, desc = "Force all currently loaded entities to stack.")
public class ForceStack extends SubCommand {

    private final StackMob sm;
    public ForceStack(StackMob sm) {
        super(CommandArgument.construct(StringArgumentType.greedyString(), true, Arrays.asList("named", "tamed", "chunk")));
        this.sm = sm;
    }

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        int count = 0;
        Predicate<LivingEntity> predicate = null;
        if (1 > 0) {
            switch (ctx.getArgument("stc", String.class).toLowerCase()) {
                case "named":
                    predicate = pEntity -> pEntity.getCustomName() != null;
                    break;
                case "tamed":
                    predicate = pEntity -> (pEntity instanceof Tameable) && ((Tameable) pEntity).isTamed();
                    break;
                case "chunk":
                    if (!(sender.getSender() instanceof Player)) {
                        sender.sendError("You need to be a player!");
                        return 2;
                    }
                    predicate = pEntity -> pEntity.getLocation().getChunk() == ((Player) sender.getSender()).getLocation().getChunk();
                    break;
            }
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
        String entityType = predicate != null ? ctx.getArgument("stc", String.class).toLowerCase() + " " : "";
        sender.sendSuccess(count + " " + entityType + "entities have been forced to stack!");
        return Command.SINGLE_SUCCESS;
    }
}
