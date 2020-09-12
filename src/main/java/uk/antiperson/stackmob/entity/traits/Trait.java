package uk.antiperson.stackmob.entity.traits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.HashSet;

public abstract class Trait implements ITrait {

    private final Collection<EntityType> supportedEntities = new HashSet<>();

    public Collection<EntityType> getSupportedEntities() {
        return supportedEntities;
    }

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return false;
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
    }

}
