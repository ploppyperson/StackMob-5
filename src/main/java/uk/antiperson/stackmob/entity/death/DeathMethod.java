package uk.antiperson.stackmob.entity.death;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

public abstract class DeathMethod implements DeathStep {

    final StackMob sm;
    private final StackEntity dead;
    public DeathMethod(StackMob sm, StackEntity dead) {
        this.sm = sm;
        this.dead = dead;
    }

    public StackEntity getDead() {
        return dead;
    }

    public LivingEntity getEntity() {
        return getDead().getEntity();
    }

    public StackMob getStackMob() {
        return sm;
    }

    @Override
    public void onSpawn(StackEntity spawned) {
        return;
    }
}
