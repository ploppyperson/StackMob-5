package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Bee;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Bee.class, path = "bee-stung")
public class BeeStung extends Trait {
    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Bee) first).hasStung() != ((Bee) nearby).hasStung();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Bee) spawned).setHasStung(((Bee) dead).hasStung());
    }
}

