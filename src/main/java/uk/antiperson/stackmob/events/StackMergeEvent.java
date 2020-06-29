package uk.antiperson.stackmob.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import uk.antiperson.stackmob.entity.StackEntity;

/**
 * Event called when two stacks merge/combine together.
 */
public class StackMergeEvent extends StackEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final StackEntity nearby;
    private boolean cancelled;
    public StackMergeEvent(StackEntity first, StackEntity nearby) {
        super(first);
        this.nearby = nearby;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the stack entity that the original stack entity is going to be merged with.
     * @return the stack entity that the original stack entity is going to be merged with.
     */
    public StackEntity getNearbyStackEntity() {
        return nearby;
    }
}
