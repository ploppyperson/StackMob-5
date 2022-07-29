package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.MushroomCow;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "mooshroom-variant")
public class MooshroomVariant extends EntityTrait<MushroomCow> {

    @Override
    public boolean checkTrait(MushroomCow first, MushroomCow nearby) {
        return first.getVariant() != nearby.getVariant();
    }

    @Override
    public void applyTrait(MushroomCow spawned, MushroomCow dead) {
        spawned.setVariant(dead.getVariant());
    }
}
