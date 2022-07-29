package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Zombie;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "age")
public class ZombieBaby extends EntityTrait<Zombie> {

    @Override
    public boolean checkTrait(Zombie first, Zombie nearby) {
        return first.isBaby() != nearby.isBaby();
    }

    @Override
    public void applyTrait(Zombie spawned, Zombie dead) {
        spawned.setBaby(dead.isBaby());
    }
}
