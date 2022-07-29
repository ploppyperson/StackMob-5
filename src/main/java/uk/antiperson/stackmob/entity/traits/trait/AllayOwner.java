package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Allay.class, path = "allay-owner")
public class AllayOwner implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return first.getMemory(MemoryKey.LIKED_PLAYER) != nearby.getMemory(MemoryKey.LIKED_PLAYER);
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        spawned.setMemory(MemoryKey.LIKED_PLAYER, dead.getMemory(MemoryKey.LIKED_PLAYER));
    }
}
