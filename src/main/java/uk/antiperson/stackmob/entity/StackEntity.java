package uk.antiperson.stackmob.entity;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.events.EventHelper;
import uk.antiperson.stackmob.utils.Utilities;

public class StackEntity {

    private final LivingEntity entity;
    private final StackMob sm;
    public StackEntity(StackMob sm, LivingEntity entity) {
        this.sm = sm;
        this.entity = entity;
    }

    /**
     * Sets the size of this stack.
     * @param newSize the size this stack should be changed to.
     */
    public void setSize(int newSize) {
        setSize(newSize, true);
    }

    /**
     * Sets the size of this stack.
     * @param newSize the size this stack should be changed to.
     */
    public void setSize(int newSize, boolean update) {
        if (newSize < 1) {
            throw new IllegalArgumentException("Stack size can not be less than one!");
        }
        if (newSize > getMaxSize()) {
            sm.getLogger().info("New stack size for entity (with id: " + getEntity().getEntityId()
                    + ") is bigger than the allowed maximum. Setting to the configured maximum value.");
            newSize = getMaxSize();
        }
        entity.getPersistentDataContainer().set(sm.getStackKey(), PersistentDataType.INTEGER, newSize);
        if (update) {
            getTag().update();
        }
    }

    public void removeStackData() {
        entity.getPersistentDataContainer().remove(sm.getStackKey());
        getTag().update();
        entity.setCustomNameVisible(false);
    }

    public boolean shouldWait(CreatureSpawnEvent.SpawnReason spawnReason) {
        if (!sm.getMainConfig().isWaitingEnabled(getEntity().getType())) {
            return false;
        }
        if (!sm.getMainConfig().getWaitingTypes(getEntity().getType()).contains(getEntity().getType().toString())) {
            return false;
        }
        if (!sm.getMainConfig().getWaitingReasons(getEntity().getType()).contains(spawnReason.toString())) {
            return false;
        }
        return true;
    }

    public void makeWait() {
        int time = sm.getMainConfig().getWaitingTime(getEntity().getType());
        getEntity().getPersistentDataContainer().set(sm.getWaitKey(), PersistentDataType.INTEGER, time);
    }

    public void incrementWait() {
        int currentWaiting = getEntity().getPersistentDataContainer().getOrDefault(sm.getWaitKey(), PersistentDataType.INTEGER, 0);
        if (currentWaiting < 1) {
            getEntity().getPersistentDataContainer().remove(sm.getWaitKey());
            setSize(1);
            return;
        }
        getEntity().getPersistentDataContainer().set(sm.getWaitKey(), PersistentDataType.INTEGER, currentWaiting - 1);
    }

    /**
     * Increments the stack size by the value given.
     * @param increment increment for stack size.
     */
    public void incrementSize(int increment) {
        setSize(getSize() + increment);
    }

    /**
     * Gets the current stack size for this entity.
     * @return the current stack size for this entity.
     */
    public int getSize() {
        return entity.getPersistentDataContainer().getOrDefault(sm.getStackKey(), PersistentDataType.INTEGER, 1);
    }

    /**
     * Gets the maximum stack size.
     * @return the maximum stack size
     */
    public int getMaxSize() {
        return sm.getMainConfig().getMaxStack(getEntity().getType());
    }

    /**
     * Removes this entity.
     */
    public void remove() {
        entity.remove();
        if (getEntity().isLeashed()) {
            ItemStack leash = new ItemStack(Material.LEAD, 1);
            getWorld().dropItemNaturally(entity.getLocation(), leash);
        }
    }

    /**
     * Returns the LivingEntity of this stack.
     * @return the LivingEntity of this stack.
     */
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * Returns the world the entity is in.
     * @return the world the entity is in.
     */
    public World getWorld() {
        return entity.getWorld();
    }

    /**
     * Returns a new instance of Tag for this entity.
     * @return a new instance of Tag for this entity.
     */
    public Tag getTag() {
        return new Tag(sm, this);
    }

    /**
     * Returns a new instance of Drops for this entity.
     * @return a new instance of Drops for this entity.
     */
    public Drops getDrops() {
        return new Drops(sm, this);
    }

    /**
     * Check if the stack is at its maximum size.
     * @return if the stack is at its maximum size.
     */
    public boolean isMaxSize() {
        return getSize() == getMaxSize();
    }

    /**
     * Check if the given entity and this entity should stack.
     * @param nearby another entity
     * @return if the given entity and this entity should stack.
     */
    public boolean checkNearby(StackEntity nearby) {
        if (getEntity().getType() != nearby.getEntity().getType()) {
            return false;
        }
        if (nearby.isMaxSize()) {
            return false;
        }
        if (sm.getTraitManager().checkTraits(this, nearby)) {
            return false;
        }
        if (sm.getHookManager().checkHooks(this, nearby)) {
            return false;
        }
        if (nearby.getEntity().isDead() || getEntity().isDead()) {
            return false;
        }
        return true;
    }

    /**
     * Merge this stack with another stack, providing they are similar.
     * @param toMerge stack to merge with.
     * @return whether the merge was successful
     */
    public boolean merge(StackEntity toMerge) {
        StackEntity entity1 = toMerge.getSize() < getSize() ? toMerge : this;
        StackEntity entity2 = toMerge.getSize() < getSize() ? this : toMerge;
        if (EventHelper.callStackMergeEvent(entity1, entity2).isCancelled()) {
            return false;
        }
        int totalSize = entity1.getSize() + entity2.getSize();
        if (totalSize > getMaxSize()) {
            toMerge.setSize(totalSize - entity2.getMaxSize());
            setSize(entity2.getMaxSize());
            return true;
        }
        entity2.incrementSize(entity1.getSize());
        entity1.remove();
        return true;
    }

    public StackEntity splitIfNotEnough(int itemAmount) {
        // If there is not enough food, then spawn a new stack with the remaining.
        if (getSize() > itemAmount) {
            return slice(itemAmount);
        }
        return null;
    }

    /**
     * Creates a clone of this entity.
     * @return a clone of this entity.
     */
    public StackEntity duplicate() {
        StackEntity cloneStack = sm.getEntityManager().getStackEntity(spawnClone());
        cloneStack.setSize(1);
        sm.getTraitManager().applyTraits(cloneStack, this);
        sm.getHookManager().onSpawn(cloneStack);
        return cloneStack;
    }

    private LivingEntity spawnClone() {
        LivingEntity entity = sm.getHookManager().spawnClone(getEntity().getLocation(), this);
        if (entity != null) {
            return entity;
        }
        if (Utilities.isPaper()) {
            return (LivingEntity) getWorld().spawnEntity(getEntity().getLocation(), getEntity().getType(), getEntity().getEntitySpawnReason());
        }
        return (LivingEntity) getWorld().spawnEntity(getEntity().getLocation(), getEntity().getType());
    }

    public boolean isSingle() {
        return getSize() < 2;
    }

    /**
     * Makes this stack smaller by 1 and spawns another stack with the remaining stack size.
     * @return stack with the remaining stack size.
     */
    public StackEntity slice() {
        return slice(1);
    }

    /**
     * Makes this stack smaller and spawns another stack with the remaining stack size.
     * @param amount amount to
     * @return stack with the remaining stack size.
     */
    public StackEntity slice(int amount) {
        if (isSingle()) {
            throw new UnsupportedOperationException("Stack size must be greater than 1 to slice!");
        }
        if (amount >= getSize()) {
            throw new UnsupportedOperationException("Slice amount is bigger than the stack size!");
        }
        StackEntity duplicate = duplicate();
        duplicate.setSize(getSize() - amount);
        setSize(amount);
        if (getEntity().isLeashed()) {
            duplicate.getEntity().setLeashHolder(getEntity().getLeashHolder());
            getEntity().setLeashHolder(null);
        }
        return duplicate;
    }

}
