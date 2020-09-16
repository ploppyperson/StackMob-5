package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Animals.class, path = "breed-mode")
public class BreedMode implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Animals) first).canBreed() != ((Animals) nearby).canBreed();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Animals) spawned).setBreed(((Animals) dead).canBreed());
    }
}
