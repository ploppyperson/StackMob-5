package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Animals;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "breed-mode")
public class BreedMode extends EntityTrait<Animals> {

    @Override
    public boolean checkTrait(Animals first, Animals nearby) {
        return first.canBreed() != nearby.canBreed();
    }

    @Override
    public void applyTrait(Animals spawned, Animals dead) {
        spawned.setBreed(dead.canBreed());
    }
}
