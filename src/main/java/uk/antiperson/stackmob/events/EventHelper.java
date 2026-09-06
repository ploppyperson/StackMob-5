package uk.antiperson.stackmob.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.Map;

public class EventHelper {

    public static StackDropItemEvent callStackDropItemEvent(
            StackEntity entity,
            Map<ItemStack, Integer> drops,
            EntityDeathEvent originalEvent
    ) {
        StackDropItemEvent event = new StackDropItemEvent(entity, drops, originalEvent);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static StackMergeEvent callStackMergeEvent(StackEntity first, StackEntity nearby) {
        StackMergeEvent event = new StackMergeEvent(first, nearby);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static StackDeathEvent callStackDeathEvent(StackEntity dead, int deathStep) {
        StackDeathEvent event = new StackDeathEvent(dead, deathStep);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static StackSpawnEvent callStackSpawnEvent(LivingEntity spawned) {
        StackSpawnEvent event = new StackSpawnEvent(spawned);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
