package uk.antiperson.stackmob.events;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.Map;

/**
 * Event called when a stack drops items.
 */
public class StackDropItemEvent extends StackEvent {

    private static final HandlerList handlers = new HandlerList();
    private Map<ItemStack, Integer> drops;
    private EntityDeathEvent originalEvent;

    public StackDropItemEvent(
            StackEntity stackEntity,
            Map<ItemStack, Integer> drops,
            EntityDeathEvent originalEvent
    ) {
        super(stackEntity);
        this.drops = drops;
        this.originalEvent = originalEvent;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the map of drops that will be dropped when the stack is killed.
     * This map should be modified to change the drops.
     * @return the map of drops that will be dropped when the stack is killed.
     */
    public Map<ItemStack, Integer> getDrops() {
        return drops;
    }

    /**
     * Returns the original event that triggered this drop item event.
     * @return the original event that triggered this drop item event.
     */
    public EntityDeathEvent getOriginalEvent() {
        return originalEvent;
    }
}
