package uk.antiperson.stackmob.hook;

import org.bukkit.entity.LivingEntity;

public interface ProtectionHook extends IHook {

    boolean canStack(LivingEntity entity);
}
