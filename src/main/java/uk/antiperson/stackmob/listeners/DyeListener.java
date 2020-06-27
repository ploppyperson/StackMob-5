package uk.antiperson.stackmob.listeners;

import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.utils.EntityUtils;

@ListenerMetadata(config = "events.dye.enabled")
public class DyeListener implements Listener {

    private final StackMob sm;

    public DyeListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDyeListener(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getRightClicked() instanceof Sheep)) {
            return;
        }
        ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!EntityUtils.isDye(handItem)) {
            return;
        }
        Sheep sheep = (Sheep) event.getRightClicked();
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(sheep);
        if (stackEntity.isSingle()) {
            return;
        }
        switch (sm.getMainConfig().getListenerMode(sheep.getType(), "dye")) {
            case SPLIT:
                StackEntity slice = stackEntity.slice();
                ((Colorable) slice.getEntity()).setColor(sheep.getColor());
                break;
            case MULTIPLY:
                stackEntity.splitIfNotEnough(event.getPlayer().getInventory().getItemInMainHand().getAmount());
                EntityUtils.removeHandItem(event.getPlayer(), stackEntity.getSize());
                sheep.setColor(DyeColor.valueOf(handItem.getType().toString().replace("_DYE", "")));
                break;
        }
    }
}
