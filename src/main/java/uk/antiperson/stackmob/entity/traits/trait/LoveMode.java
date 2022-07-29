package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Animals;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "love-mode")
public class LoveMode extends EntityTrait<Animals> {

    @Override
    public boolean checkTrait(Animals first, Animals nearby) {
        return first.getLoveModeTicks() != 0 || nearby.getLoveModeTicks() != 0;
    }

    @Override
    public void applyTrait(Animals spawned, Animals dead) {
        spawned.setLoveModeTicks(spawned.getLoveModeTicks());
    }
}
