package uk.antiperson.stackmob.listeners;

import org.bukkit.event.Listener;
import uk.antiperson.stackmob.StackMob;

public class BeeListener implements Listener {

    private StackMob sm;

    public BeeListener(StackMob sm) {
        this.sm = sm;
    }

    /*public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        if (event.getEntityType() != EntityType.BEE) return;

        final Bee bee = (Bee) event.getEntity();
        final StackEntity oldBeeStackEntity = sm.getEntityManager().getStackEntity(bee);

        //TODO Waiting for Spigot's reply -> Working but it's not a complete reliable solution (latencies or NBT issues would make it broken)
        boolean a = NBTEditor.getBlockNBTTag(bee.getHive().getBlock(), "Bees").toString().contains(NBTEditor.getEntityNBTTag(bee).toString());
        Bukkit.broadcastMessage(a + "");
        if (bee.getHive() != null || oldBeeStackEntity.isSingle()) return; //TODO Check if the entity is not dead or remove by a plugin / by chunk unloading !

        final StackEntity newBeeStackEntity = oldBeeStackEntity.duplicate();
        newBeeStackEntity.setSize(oldBeeStackEntity.getSize() - 1);
        ((Bee) newBeeStackEntity.getEntity()).setHasNectar(false);
    }*/

}
