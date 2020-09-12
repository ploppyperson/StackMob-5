package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Sheep.class, path = "sheep-color")
public class SheepColor extends Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Sheep) first).getColor() != ((Sheep) nearby).getColor();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Sheep) spawned).setColor(((Sheep) dead).getColor());
    }
}
