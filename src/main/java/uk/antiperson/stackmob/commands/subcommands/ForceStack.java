package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;
import uk.antiperson.stackmob.commands.User;
import uk.antiperson.stackmob.utils.Utilities;

@CommandMetadata(command = "forcestack", playerReq = false, desc = "Force all currently loaded entities to stack")
public class ForceStack extends SubCommand {

    private StackMob sm;
    public ForceStack(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(User sender, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getEntitiesByClass(Mob.class)) {
                if (sm.getEntityManager().isStackedEntity(entity)) {
                    continue;
                }
                CreatureSpawnEvent.SpawnReason reason = Utilities.isPaper() ? entity.getEntitySpawnReason() : null;
                if (sm.getMainConfig().isEntityBlacklisted(entity, reason)) {
                    continue;
                }
                sm.getEntityManager().getStackEntity(entity).setSize(1);
            }
        }
        return false;
    }
}
