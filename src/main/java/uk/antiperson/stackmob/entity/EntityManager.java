package uk.antiperson.stackmob.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import uk.antiperson.stackmob.StackMob;

public class EntityManager {

    private final StackMob sm;
    public EntityManager(StackMob sm) {
        this.sm = sm;
    }

    public boolean isStackedEntity(LivingEntity entity) {
        return entity.getPersistentDataContainer().has(sm.getStackKey(), PersistentDataType.INTEGER);
    }

    public StackEntity getStackEntity(LivingEntity entity) {
        return new StackEntity(sm, entity);
    }

    public boolean isWaiting(LivingEntity entity) {
        return entity.getPersistentDataContainer().has(sm.getWaitKey(), PersistentDataType.INTEGER);
    }
}
