package uk.antiperson.stackmob.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import uk.antiperson.stackmob.StackMob;

public class ChunkListener implements Listener {

    private StackMob sm;
    public ChunkListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        StackMob.getEntityManager().registerStackedEntities(event.getChunk());
    }

}