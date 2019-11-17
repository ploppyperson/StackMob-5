package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Mob.class, path = "disable-ai.enabled")
public class MobAi implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return false;
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        spawned.setAI(dead.hasAI());
    }
}
