package uk.antiperson.stackmob.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.concurrent.ThreadLocalRandom;

@ListenerMetadata(config = "events.multiply.slime-split")
public class SlimeListener implements Listener {

    private StackMob sm;
    public SlimeListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSlimeSplit(SlimeSplitEvent event) {
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(event.getEntity());
        if (stackEntity.isSingle()) {
            return;
        }
        if (!event.getEntity().hasMetadata("deathcount")) {
            return;
        }
        int deathAmount = event.getEntity().getMetadata("deathcount").get(0).asInt();
        for (int i = 0; i < deathAmount; i++) {
            int randSlime = ThreadLocalRandom.current().nextInt(2,4);
            event.setCount(event.getCount() + randSlime);
        }
        event.getEntity().removeMetadata("deathcount", sm);
    }
}
