package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Frog;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Frog.class, path = "frog-variant")
public class FrogVariant implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Frog) first).getVariant() != ((Frog) nearby).getVariant();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Frog) spawned).setVariant(((Frog) dead).getVariant());
    }
}
