package uk.antiperson.stackmob.listeners;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import uk.antiperson.stackmob.StackMob;

@ListenerMetadata(config = "traits.leashed")
public class LeashListener implements Listener {

    private final StackMob sm;
    public LeashListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeash(EntityDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() != Material.LEAD) {
            return;
        }
        if (!StackMob.getEntityManager().isStackedEntity((LivingEntity) event.getEntity())) {
            return;
        }
        if (!event.getEntity().isDead()) {
            return;
        }
        event.setCancelled(true);
    }

}
