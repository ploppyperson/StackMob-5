package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Frog;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "frog-variant")
public class FrogVariant extends EntityTrait<Frog> {

    @Override
    public boolean checkTrait(Frog first, Frog nearby) {
        return first.getVariant() != nearby.getVariant();
    }

    @Override
    public void applyTrait(Frog spawned, Frog dead) {
        spawned.setVariant(dead.getVariant());
    }
}
