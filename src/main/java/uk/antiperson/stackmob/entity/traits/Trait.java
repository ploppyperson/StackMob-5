package uk.antiperson.stackmob.entity.traits;

import org.bukkit.entity.LivingEntity;

public interface Trait {

    /**
     * Check if two entities have the same entity specific traits (eg. sheep colour, villager profession)
     * @param first the initial entity.
     * @param nearby the entity the first should stack with
     * @return whether these two entities should stack.
     */
    boolean checkTrait(LivingEntity first, LivingEntity nearby);

    /**
     * Copy the traits of the dead entity to that of the newly spawned entity.
     * @param dead the entity that died.
     * @param spawned the entity that was spawned to replace it.
     */
    void applyTrait(LivingEntity spawned, LivingEntity dead);

}
