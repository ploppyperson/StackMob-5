package uk.antiperson.stackmob.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

import java.util.Arrays;
import java.util.HashSet;

public class TemporaryCompat extends BukkitRunnable implements Listener {

    private final HashSet<Chunk> chunks;
    private final StackMob sm;

    public TemporaryCompat(StackMob sm) {
        this.sm = sm;
        this.chunks = new HashSet<>();
    }

    public void init() {
        for (World world : Bukkit.getWorlds())  {
            chunks.addAll(Arrays.asList(world.getLoadedChunks()));
        }
    }

    @Override
    public void run() {
        HashSet<Chunk> removed = new HashSet<>();
        for (Chunk chunk : chunks) {
            if (chunk.getEntities().length == 0) {
                continue;
            }
            sm.getEntityManager().registerStackedEntities(chunk);
            removed.add(chunk);
        }
        chunks.removeAll(removed);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        chunks.add(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        chunks.remove(event.getChunk());
    }
}
