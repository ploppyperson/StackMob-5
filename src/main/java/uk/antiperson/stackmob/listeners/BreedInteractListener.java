package uk.antiperson.stackmob.listeners;

import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.utils.EntityUtils;

import java.util.concurrent.ThreadLocalRandom;

@ListenerMetadata(config = "breed.enabled")
public class BreedInteractListener implements Listener {

    private StackMob sm;
    public BreedInteractListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onBreedInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getRightClicked() instanceof Animals)) {
            return;
        }
        if (!((Animals) event.getRightClicked()).canBreed()) {
            return;
        }
        ItemStack foodItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!EntityUtils.isCorrectFood(event.getRightClicked(), foodItem.getType())) {
            return;
        }
        Animals animals = (Animals) event.getRightClicked();
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(animals);
        if (stackEntity.isSingle()) {
            return;
        }
        switch (sm.getMainConfig().getListenerMode(animals.getType(), "breed")) {
            case SPLIT:
                stackEntity.slice();
                break;
            case MULTIPLY:
                int itemAmount = event.getPlayer().getInventory().getItemInMainHand().getAmount();
                EntityUtils.removeHandItem(event.getPlayer(), stackEntity.getSize());
                stackEntity.splitIfNotEnough(itemAmount);
                if (itemAmount == 1) {
                    return;
                }
                double kAmount = stackEntity.getSize() / 2D;
                int kidAmount = (int) Math.floor(kAmount);
                if (kAmount > kidAmount) {
                    stackEntity.duplicate();
                    stackEntity.incrementSize(-1);
                }
                stackEntity.getDrops().dropExperience(event.getRightClicked().getLocation(),1,7, kidAmount);
                // Spawn the kid
                StackEntity kid = stackEntity.duplicate();
                kid.setSize(kidAmount);
                ((Animals) kid.getEntity()).setBaby();
                // Update the adult
                animals.setBreed(false);
                animals.setBreedCause(event.getPlayer().getUniqueId());
                break;
        }
    }


}
