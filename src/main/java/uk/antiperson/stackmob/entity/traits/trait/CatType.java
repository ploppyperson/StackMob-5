package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Cat;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "cat-type")
public class CatType extends EntityTrait<Cat> {

    @Override
    public boolean checkTrait(Cat first, Cat nearby) {
        return first.getCatType() != nearby.getCatType();
    }

    @Override
    public void applyTrait(Cat spawned, Cat dead) {
        spawned.setCatType(dead.getCatType());
    }
}
