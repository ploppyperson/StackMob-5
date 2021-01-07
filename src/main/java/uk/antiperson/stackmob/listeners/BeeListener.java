package uk.antiperson.stackmob.listeners;

import org.bukkit.entity.Bee;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

@ListenerMetadata(config = "events.divide.enter-block")
public class BeeListener implements Listener {

    private final StackMob sm;

    public BeeListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityEnterBlockEvent(EntityEnterBlockEvent event) {
        final Bee oldBee = (Bee) event.getEntity();
        if (!sm.getEntityManager().isStackedEntity(oldBee)) {
            return;
        }
        final StackEntity oldStackedBee = sm.getEntityManager().getStackEntity(oldBee);
        if (oldStackedBee == null || oldStackedBee.isSingle()) {
            return;
        }
        event.setCancelled(true);

        final Bee newBee = ((Bee) oldStackedBee.slice().getEntity());
        newBee.setCannotEnterHiveTicks(2);
        oldStackedBee.removeStackData();
        sm.getServer().getScheduler().runTaskLater(sm, () -> {
            if (oldBee.isValid()) {
                oldBee.setCannotEnterHiveTicks(0);
            }
        }, 1L);
    }

}
