package uk.antiperson.stackmob.listeners;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Turtle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.Drops;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.concurrent.ThreadLocalRandom;

@ListenerMetadata(config = "multiply.drops")
public class DropListener implements Listener {

    private StackMob sm;

    public DropListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDropListener(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Chicken) || !(event.getEntity() instanceof Turtle)) {
            return;
        }
        StackEntity entity = sm.getEntityManager().getStackEntity((LivingEntity) event.getEntity());
        double multiplier = ThreadLocalRandom.current().nextDouble(0.4, 0.8);
        int itemCount = (int) Math.round((entity.getSize() - 1) * multiplier);
        Drops.dropItem(event.getEntity().getLocation(), event.getItemDrop().getItemStack(), itemCount);
    }
}
