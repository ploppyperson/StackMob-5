package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Cat;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Cat.class, path = "cat-type")
public class CatType implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Cat) first).getCatType() != ((Cat) nearby).getCatType();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Cat) spawned).setCatType(((Cat) dead).getCatType());
    }
}
