package uk.antiperson.stackmob.entity;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import uk.antiperson.stackmob.StackMob;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EntityManager {

    private final Map<UUID, Integer> sizeCache = ExpiringMap.builder().maxSize(10000).expiration(30, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.ACCESSED).build();
    private final StackMob sm;
    public EntityManager(StackMob sm) {
        this.sm = sm;
    }

    public boolean isStackedEntity(LivingEntity entity) {
        return entity.getPersistentDataContainer().has(sm.getStackKey(), PersistentDataType.INTEGER);
    }

    public StackEntity getStackEntity(LivingEntity entity) {
        Integer size = sizeCache.get(entity.getUniqueId());
        return size == null ? new StackEntity(sm, entity) : new StackEntity(sm, entity, size);
    }

    public boolean isWaiting(LivingEntity entity) {
        return entity.getPersistentDataContainer().has(sm.getWaitKey(), PersistentDataType.INTEGER);
    }

    public Map<UUID, Integer> getSizeCache() {
        return sizeCache;
    }

}
