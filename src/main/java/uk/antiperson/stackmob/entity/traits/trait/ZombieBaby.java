package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Zombie.class, path = "age")
public class ZombieBaby extends Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Zombie) first).isBaby() != ((Zombie) nearby).isBaby();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Zombie) spawned).setBaby(((Zombie) dead).isBaby());
    }
}
