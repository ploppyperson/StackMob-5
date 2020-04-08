package uk.antiperson.stackmob.entity.death;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

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
        new BukkitRunnable() {
            @Override
            public void run() {
                AttributeInstance maxHealthInstance = getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH);
                double maxHealthWithModifiers = maxHealthInstance.getValue();
                double maxHealthWithoutModifiers = maxHealthInstance.getBaseValue();
                spawned.getEntity().setHealth(Math.min(maxHealthWithModifiers - leftOverDamage, maxHealthWithoutModifiers));
            }
        }.runTaskLater(getStackMob(), 1L);
    }
}
