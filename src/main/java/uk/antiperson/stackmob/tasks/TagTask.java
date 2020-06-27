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
import uk.antiperson.stackmob.utils.NMSHelper;

import java.util.List;
import java.util.logging.Level;

public class TagTask extends BukkitRunnable {

    private final StackMob sm;
    public TagTask(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public void run() {
        Integer[] searchRadius = sm.getMainConfig().getTagNeabyRadius();
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<Entity> furthest = player.getNearbyEntities(searchRadius[0] * 1.5, searchRadius[1] * 1.5, searchRadius[2] * 1.5);
            List<Entity> nearest = player.getNearbyEntities(searchRadius[0], searchRadius[1], searchRadius[2]);
            furthest.removeAll(nearest);
            furthest.forEach(entity -> sendPacket(player, entity, false));
            nearest.forEach(entity -> sendPacket(player, entity, true));
        }
    }

    private void sendPacket(Player player, Entity entity, boolean tagVisible) {
        if (!(entity instanceof Mob)) {
            return;
        }
        if (entity.isDead()) {
            return;
        }
        if (!sm.getEntityManager().isStackedEntity((LivingEntity) entity)) {
            return;
        }
        if (sm.getMainConfig().getTagMode(entity.getType()) != TagMode.NEARBY) {
            return;
        }
        StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) entity);
        int threshold = sm.getMainConfig().getTagThreshold(entity.getType());
        if (stackEntity.getSize() <= threshold) {
            return;
        }
        try {
            NMSHelper.sendPacket(player, entity, tagVisible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            sm.getLogger().log(Level.WARNING,"An error occurred while sending packet. Is StackMob updated to support your server version?");
        }
    }
}
