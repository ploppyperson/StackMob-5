package uk.antiperson.stackmob.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

@ListenerMetadata(config = "events.equip.enabled")
public class EquipListener implements Listener {

    private final StackMob sm;
    public EquipListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEntityEquip(EntityPickupItemEvent event) {
        if (!sm.getEntityManager().isStackedEntity(event.getEntity())) {
            return;
        }
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(event.getEntity());
        stackEntity.addEquipItem(event.getItem().getItemStack());
    }

}