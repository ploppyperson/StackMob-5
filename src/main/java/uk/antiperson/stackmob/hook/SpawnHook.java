package uk.antiperson.stackmob.hook;

import org.bukkit.entity.LivingEntity;

public interface SpawnHook extends IHook {

    void onSpawn(LivingEntity spawned);
}
