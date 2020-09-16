package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = MushroomCow.class, path = "mooshroom-variant")
public class MooshroomVariant implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((MushroomCow) first).getVariant() != ((MushroomCow) nearby).getVariant();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((MushroomCow) spawned).setVariant(((MushroomCow) dead).getVariant());
    }
}
