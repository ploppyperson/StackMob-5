package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Snowman;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "snowman-sheared")
public class SnowmanShear implements Trait<Snowman> {
    @Override
    public boolean checkTrait(Snowman first, Snowman nearby) {
        return first.isDerp() == nearby.isDerp();
    }

    @Override
    public void applyTrait(Snowman spawned, Snowman dead) {
        spawned.setDerp(dead.isDerp());
    }
}
