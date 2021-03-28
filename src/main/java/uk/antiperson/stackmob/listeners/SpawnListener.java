package uk.antiperson.stackmob.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.events.EventHelper;

public class SpawnListener implements Listener {

    private final StackMob sm;

    public SpawnListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Mob)) {
            return;
        }
        sm.getServer().getScheduler().runTask(sm, () -> {
            final LivingEntity eventEntity = event.getEntity();
            if (!eventEntity.isValid()) {
                return;
            }
            if (sm.getMainConfig().isEntityBlacklisted(eventEntity, event.getSpawnReason())) {
                return;
            }
            if (sm.getEntityManager().isStackedEntity(eventEntity)) {
                final StackEntity stackEntity = sm.getEntityManager().getStackEntity(event.getEntity());
                if (stackEntity != null && stackEntity.isForgetOnSpawn()) {
                    stackEntity.removeStackData();
                }
                return;
            }
            if (EventHelper.callStackSpawnEvent(eventEntity).isCancelled()) {
                return;
            }
            final StackEntity original = sm.getEntityManager().registerStackedEntity(eventEntity);
            if (original.shouldWait(event.getSpawnReason())) {
                original.makeWait();
                return;
            }
            final Integer[] searchRadius = sm.getMainConfig().getStackRadius(eventEntity.getType());
            for (Entity entity : eventEntity.getNearbyEntities(searchRadius[0], searchRadius[1], searchRadius[2])) {
                if (!(entity instanceof Mob)) {
                    continue;
                }
                final StackEntity nearby = sm.getEntityManager().getStackEntity((LivingEntity) entity);
                if (nearby == null) {
                    continue;
                }
                if (!nearby.canStack()) {
                    continue;
                }
                if (!original.match(nearby)) {
                    continue;
                }
                if (sm.getMainConfig().getStackThresholdEnabled(entity.getType()) && nearby.getSize() == 1) {
                    continue;
                }
                original.merge(nearby, true);
                return;
            }
            original.setSize(1, eventEntity.getCustomName() == null);
            sm.getHookManager().onSpawn(original);
        });
    }

}
