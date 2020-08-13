package uk.antiperson.stackmob.entity;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;
import uk.antiperson.stackmob.StackMob;

import java.util.Collection;
import java.util.HashSet;

public class EntityManager {

    private final StackMob sm;
    private final HashSet<StackEntity> stackEntities;
    public EntityManager(StackMob sm) {
        this.sm = sm;
        stackEntities = new HashSet<>();
    }

    public boolean isStackedEntity(LivingEntity entity) {
        return entity.getPersistentDataContainer().has(sm.getStackKey(), PersistentDataType.INTEGER);
    }

    public HashSet<StackEntity> getStackEntities() {
        return stackEntities;
    }

    public StackEntity getStackEntity(LivingEntity entity) {
        for (StackEntity stackEntity : stackEntities) {
            if (stackEntity.getEntity().getUniqueId().equals(entity.getUniqueId())) {
                return stackEntity;
            }
        }
        return null;
    }

    public void registerAllEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                registerStackedEntities(chunk);
            }
        }
    }

    public void unregisterAllEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                unregisterStackedEntities(chunk);
            }
        }
    }

    public void registerStackedEntities(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Mob)) {
                continue;
            }
            if (!sm.getEntityManager().isStackedEntity((LivingEntity) entity)) {
                continue;
            }
            sm.getEntityManager().registerStackedEntity((LivingEntity) entity);
        }
    }

    public void unregisterStackedEntities(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Mob)) {
                continue;
            }
            if (!sm.getEntityManager().isStackedEntity((LivingEntity) entity)) {
                continue;
            }
            sm.getEntityManager().unregisterStackedEntity((LivingEntity) entity);
        }
    }

    public StackEntity registerStackedEntity(LivingEntity entity) {
        StackEntity stackEntity = new StackEntity(sm, this, entity);
        stackEntities.add(stackEntity);
        return stackEntity;
    }

    public void registerStackedEntity(StackEntity entity) {
        stackEntities.add(entity);
    }

    public void unregisterStackedEntity(LivingEntity entity) {
        StackEntity stackEntity = getStackEntity(entity);
        if (stackEntity == null) {
            throw new UnsupportedOperationException("Attempted to unregister entity that isn't stacked!");
        }
        unregisterStackedEntity(stackEntity);
    }

    public void unregisterStackedEntity(StackEntity stackEntity) {
        stackEntities.remove(stackEntity);
    }

}
