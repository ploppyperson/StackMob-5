package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Mob.class, path = "leashed")
public class Leash implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        if (!first.isLeashed() || !nearby.isLeashed()) {
            return false;
        }
        if (first.getLeashHolder() != nearby.getLeashHolder()) {
            return true;
        }
        return false;
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        if (!dead.isLeashed()) {
            return;
        }
        spawned.setLeashHolder(dead.getLeashHolder());
    }
}
