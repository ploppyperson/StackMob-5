package uk.antiperson.stackmob.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.utils.Utilities;

@ListenerMetadata(config = "disable-targeting.enabled")
public class TargetListener implements Listener {

    private StackMob sm;
    public TargetListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        if (!sm.getEntityManager().isStackedEntity((LivingEntity) event.getEntity())) {
            return;
        }
        if (!sm.getMainConfig().isTargetingDisabled(event.getEntityType())) {
            return;
        }
        if (sm.getMainConfig().isTargetingDisabledType(event.getEntityType())) {
            return;
        }
        if (Utilities.isPaper()) {
            if (sm.getMainConfig().isTargetingDisabledReason(event.getEntityType(), event.getReason())) {
                return;
            }
        }
        event.setCancelled(true);
    }
}
