package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Piglin.class, path = "piglin-baby")
public class PiglinBaby implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Piglin) first).isBaby() != ((Piglin) nearby).isBaby();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Piglin) spawned).setBaby(((Piglin) dead).isBaby());
    }
}
