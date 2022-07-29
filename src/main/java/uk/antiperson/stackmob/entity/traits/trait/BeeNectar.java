package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Bee;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "bee-nectar")
public class BeeNectar extends EntityTrait<Bee> {
    @Override
    public boolean checkTrait(Bee first, Bee nearby) {
        return first.hasNectar() != nearby.hasNectar();
    }

    @Override
    public void applyTrait(Bee spawned, Bee dead) {
        spawned.setHasNectar(spawned.hasNectar());
    }
}
