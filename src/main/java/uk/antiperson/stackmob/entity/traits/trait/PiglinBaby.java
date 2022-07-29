package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Piglin;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "piglin-baby")
public class PiglinBaby extends EntityTrait<Piglin> {

    @Override
    public boolean checkTrait(Piglin first, Piglin nearby) {
        return first.isBaby() != nearby.isBaby();
    }

    @Override
    public void applyTrait(Piglin spawned, Piglin dead) {
        spawned.setBaby(dead.isBaby());
    }
}
