package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Animals.class, path = "love-mode")
public class LoveMode implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Animals) first).getLoveModeTicks() != 0 || ((Animals) nearby).getLoveModeTicks() != 0;
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Animals) spawned).setLoveModeTicks(((Animals) spawned).getLoveModeTicks());
    }
}
