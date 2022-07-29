package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Villager;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "villager-profession")
public class VillagerProfession extends EntityTrait<Villager> {

    @Override
    public boolean checkTrait(Villager first, Villager nearby) {
        return first.getProfession() != nearby.getProfession();
    }

    @Override
    public void applyTrait(Villager spawned, Villager dead) {
        spawned.setProfession(dead.getProfession());
    }
}
