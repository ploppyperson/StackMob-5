package uk.antiperson.stackmob.hook;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface StackableMobHook extends CustomMobHook {

    boolean isMatching(LivingEntity first, LivingEntity nearby);

    LivingEntity spawnClone(Location location, LivingEntity dead);

    String getDisplayName(LivingEntity entity);
}
