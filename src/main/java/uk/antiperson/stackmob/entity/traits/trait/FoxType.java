package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Fox.class, path = "fox-type")
public class FoxType extends Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Fox) first).getFoxType() != ((Fox) nearby).getFoxType();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Fox) spawned).setFoxType(((Fox) dead).getFoxType());
    }
}
