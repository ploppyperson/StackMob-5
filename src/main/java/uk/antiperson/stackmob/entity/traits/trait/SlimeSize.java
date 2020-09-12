package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Slime.class, path = "slime-size")
public class SlimeSize extends Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Slime) first).getSize() != ((Slime) nearby).getSize();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Slime) spawned).setSize(((Slime) dead).getSize());
    }
}
