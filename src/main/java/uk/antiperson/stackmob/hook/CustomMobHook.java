package uk.antiperson.stackmob.hook;

import org.bukkit.entity.LivingEntity;

public interface CustomMobHook extends IHook {

    boolean isCustomMob(LivingEntity entity);
}
