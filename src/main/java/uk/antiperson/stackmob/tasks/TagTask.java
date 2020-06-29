package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.entity.TagMode;

import java.util.List;

public class TagTask extends BukkitRunnable {

    private final StackMob sm;
    public TagTask(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public void run() {
        Integer[] searchRadius = sm.getMainConfig().getTagNearbyRadius();
        double searchX = searchRadius[0];
        double searchY = searchRadius[1];
        double searchZ = searchRadius[2];
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<Entity> entities = player.getNearbyEntities(searchX * 1.5, searchY * 1.5, searchZ * 1.5);
            for (Entity entity : entities) {
                if (!(entity instanceof Mob)) {
                    continue;
                }
                if (!sm.getEntityManager().isStackedEntity((LivingEntity) entity)) {
                    continue;
                }
                if (entity.isDead()) {
                    continue;
                }
                if (sm.getMainConfig().getTagMode(entity.getType()) != TagMode.NEARBY) {
                    return;
                }
                StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) entity);
                int threshold = sm.getMainConfig().getTagThreshold(stackEntity.getEntity().getType());
                if (stackEntity.getSize() <= threshold) {
                    return;
                }
                double xDiff = Math.abs(player.getLocation().getX() - entity.getLocation().getX());
                double yDiff = Math.abs(player.getLocation().getY() - entity.getLocation().getY());
                double zDiff = Math.abs(player.getLocation().getZ() - entity.getLocation().getZ());
                if (xDiff < searchX && yDiff < searchY && zDiff < searchZ) {
                    // Player should be shown tag
                    stackEntity.getTag().sendPacket(player, true);
                    continue;
                }
                // Player should not be shown tag
                stackEntity.getTag().sendPacket(player, false);
            }
        }
    }

}
