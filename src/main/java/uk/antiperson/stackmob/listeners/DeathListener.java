package uk.antiperson.stackmob.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.entity.Drops;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.entity.death.DeathMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DeathListener implements Listener {

    private StackMob sm;
    public DeathListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onStackDeath(EntityDeathEvent event) {
        if (!sm.getEntityManager().isStackedEntity(event.getEntity())) {
            return;
        }
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(event.getEntity());
        if (stackEntity.getSize() == 1) {
            return;
        }
        DeathMethod deathMethod = calculateDeath(stackEntity);
        int deathStep = deathMethod.calculateStep();
        if (deathStep < stackEntity.getSize()) {
            StackEntity spawned = stackEntity.duplicate();
            spawned.setSize(stackEntity.getSize() - deathStep);
            deathMethod.onSpawn(spawned);
        }
        if (deathStep > 1) {
            Map<ItemStack, Integer> drops = stackEntity.getDrops().calculateDrops(deathStep - 1, event.getDrops());
            Drops.dropItems(event.getEntity().getLocation(), drops);
            int experience = stackEntity.getDrops().calculateExperience(deathStep - 1, event.getDroppedExp());
            event.setDroppedExp(experience);
        }
    }

    public DeathMethod calculateDeath(StackEntity entity){
        DeathType deathType = sm.getMainConfig().getDeathType(entity.getEntity());
        try {
            return deathType.getStepClass().getDeclaredConstructor(StackMob.class, StackEntity.class).newInstance(sm, entity);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Error while determining death step!");
        }
    }




}
