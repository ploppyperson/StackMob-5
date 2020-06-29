package uk.antiperson.stackmob.events;

import org.bukkit.event.Event;
import uk.antiperson.stackmob.entity.StackEntity;

public abstract class StackEvent extends Event {

    private final StackEntity stackEntity;
    public StackEvent(StackEntity stackEntity) {
        this.stackEntity = stackEntity;
    }

    /**
     * Gets the stack that was involved in this event.
     * @return the stack that was involved in this event.
     */
    public StackEntity getStackEntity() {
        return stackEntity;
    }
}
