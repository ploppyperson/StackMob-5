package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Parrot.class, path = "parrot-variant")
public class ParrotVariant implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Parrot) first).getVariant() != ((Parrot) nearby).getVariant();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Parrot) spawned).setVariant(((Parrot) spawned).getVariant());
    }
}
