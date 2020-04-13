package uk.antiperson.stackmob.entity.death;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.hook.StackableMobHook;
import uk.antiperson.stackmob.hook.hooks.MythicMobsHook;

public class KillStepDamage extends DeathMethod {

    private double leftOverDamage;
    public KillStepDamage(StackMob sm, StackEntity dead) {
        super(sm, dead);
    }

    @Override
    public int calculateStep() {
        double healthBefore = ((LivingEntity)getDead().getEntity().getLastDamageCause().getEntity()).getHealth();
        double damageDone = getEntity().getLastDamageCause().getFinalDamage();
        double damageLeft = Math.abs(healthBefore - damageDone);
        double maxHealth = getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double divided = damageLeft / maxHealth;
        int entities = (int) Math.floor(divided);
        leftOverDamage = (divided - entities) * maxHealth;
        return entities + 1;
    }

    @Override
    public void onSpawn(StackEntity spawned) {
        double maxHealth = getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        StackableMobHook smh = sm.getHookManager().getApplicableHook(spawned);
        if (smh instanceof MythicMobsHook) {
            sm.getServer().getScheduler().runTaskLater(sm, bukkitTask -> {
                if (!spawned.getEntity().isDead()) {
                    spawned.getEntity().setHealth(maxHealth - leftOverDamage);
                }
            }, 5);
        }
        spawned.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        spawned.getEntity().setHealth(maxHealth - leftOverDamage);
    }
}
