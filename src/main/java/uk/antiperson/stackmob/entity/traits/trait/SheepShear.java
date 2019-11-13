package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Sheep.class, path = "sheep-sheared")
public class SheepShear implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Sheep) first).isSheared() != ((Sheep) nearby).isSheared();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Sheep) spawned).setSheared(((Sheep) dead).isSheared());
    }
}
