package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Ageable.class, path = "age")
public class Age implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Ageable) first).isAdult() != ((Ageable) nearby).isAdult();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Ageable) spawned).setAge(((Ageable) dead).getAge());
    }
}
