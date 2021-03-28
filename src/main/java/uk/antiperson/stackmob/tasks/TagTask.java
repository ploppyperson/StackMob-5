package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

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
            for (Entity entity : player.getNearbyEntities(searchX * 1.5, searchY * 1.5, searchZ * 1.5)) {
                if (!(entity instanceof Mob)) {
                    continue;
                }
                if (entity.isDead()) {
                    continue;
                }
                if (sm.getMainConfig().getTagMode(entity.getType()) != StackEntity.TagMode.NEARBY) {
                    return;
                }
                final StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) entity);
                if (stackEntity == null) {
                    return;
                }
                final int threshold = sm.getMainConfig().getTagThreshold(stackEntity.getEntity().getType());
                if (stackEntity.getSize() <= threshold) {
                    return;
                }
                final double xDiff = Math.abs(player.getLocation().getX() - entity.getLocation().getX());
                final double yDiff = Math.abs(player.getLocation().getY() - entity.getLocation().getY());
                final double zDiff = Math.abs(player.getLocation().getZ() - entity.getLocation().getZ());
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
