package uk.antiperson.stackmob.entity;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.events.EventHelper;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.Set;

public class StackEntity {

    private static final EntityManager entityManager = StackMob.getInstance().getEntityManager();

    private final LivingEntity entity;
    private final StackMob sm;
    private boolean waiting;
    private int waitCount;
    private int stackSize;
    private Set<ItemStack> equiptItems;

    public StackEntity(StackMob sm, LivingEntity entity) {
        this.sm = sm;
        this.entity = entity;
    }

    /**
     * Sets the size of this stack.
     *
     * @param newSize the size this stack should be changed to.
     */
    public void setSize(int newSize) {
        setSize(newSize, true);
    }

    /**
     * Sets the size of this stack.
     *
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
        stackSize = newSize;
        if (update) {
            getTag().update();
        }
    }

    public void removeStackData() {
        entity.getPersistentDataContainer().remove(sm.getStackKey());
        entity.setCustomNameVisible(false);
        entityManager.unregisterStackedEntity(this);
        getTag().update();
    }

    /**
     * In order to not break mob grinders, stacked entities can have a waiting status.
     * This waiting status means that this stacked entity will be ignored on all stacking attempts until the count reaches 0
     *
     * @return whether this entity is currently waiting
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * Gets the wait count for this entity.
     *
     * @return the wait count for this entity
     */
    public int getWaitCount() {
        return waitCount;
    }

    /**
     * Whether this entity should wait.
     *
     * @param spawnReason the spawn reason of the entity.
     * @return whether this entity should wait.
     */
    public boolean shouldWait(CreatureSpawnEvent.SpawnReason spawnReason) {
        if (!sm.getMainConfig().isWaitingEnabled(getEntity().getType())) {
            return false;
        }
        if (!sm.getMainConfig().isWaitingType(getEntity().getType())) {
            return false;
        }
        return sm.getMainConfig().isWaitingReason(getEntity().getType(), spawnReason);
    }

    /**
     * In order to not break mob grinders, stacked entities can have a waiting status.
     * This waiting status means that this stacked entity will be ignored on all stacking attempts until the count reaches 0.
     */
    public void makeWait() {
        if (isWaiting()) {
            throw new UnsupportedOperationException("Stack is already waiting!");
        }
        waitCount = sm.getMainConfig().getWaitingTime(getEntity().getType());
        waiting = true;
    }

    /**
     * Increment the waiting count.
     */
    public void incrementWait() {
        if (waitCount < 1) {
            waiting = false;
            setSize(1);
            return;
        }
        waitCount -= 1;
    }

    /**
     * Increments the stack size by the value given.
     *
     * @param increment increment for stack size.
     */
    public void incrementSize(int increment) {
        setSize(getSize() + increment);
    }

    /**
     * Gets the current stack size for this entity.
     *
     * @return the current stack size for this entity.
     */
    public int getSize() {
        if (stackSize == 0) {
            stackSize = getEntity().getPersistentDataContainer().getOrDefault(sm.getStackKey(), PersistentDataType.INTEGER, 1);
        }
        return stackSize;
    }

    /**
     * Gets the maximum stack size.
     *
     * @return the maximum stack size
     */
    public int getMaxSize() {
        return sm.getMainConfig().getMaxStack(getEntity().getType());
    }

    /**
     * Removes this entity.
     */
    public void remove() {
        remove(true);
    }

    public void remove(boolean unregister) {
        entity.remove();
        if (unregister) {
            entityManager.unregisterStackedEntity(this);
        }
        if (getEntity().isLeashed()) {
            ItemStack leash = new ItemStack(Material.LEAD, 1);
            getWorld().dropItemNaturally(entity.getLocation(), leash);
        }
        dropEquipItems();
    }

    /**
     * Returns the LivingEntity of this stack.
     *
     * @return the LivingEntity of this stack.
     */
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * Returns the world the entity is in.
     *
     * @return the world the entity is in.
     */
    public World getWorld() {
        return entity.getWorld();
    }

    /**
     * Returns a new instance of Tag for this entity.
     *
     * @return a new instance of Tag for this entity.
     */
    public Tag getTag() {
        return new Tag(sm, this);
    }

    /**
     * Returns a new instance of Drops for this entity.
     *
     * @return a new instance of Drops for this entity.
     */
    public Drops getDrops() {
        return new Drops(sm, this);
    }

    /**
     * Check if the stack is at its maximum size.
     *
     * @return if the stack is at its maximum size.
     */
    public boolean isMaxSize() {
        return getSize() == getMaxSize();
    }

    /**
     * Check if the given entity and this entity should stack.
     *
     * @param nearby another entity
     * @return if the given entity and this entity should stack.
     */
    public boolean match(StackEntity nearby) {
        if (getEntity().getType() != nearby.getEntity().getType()) {
            return false;
        }
        if (sm.getTraitManager().checkTraits(this, nearby)) {
            return false;
        }
        return !sm.getHookManager().checkHooks(this, nearby);
    }

    public boolean canStack() {
        if (equiptItems != null && !equiptItems.isEmpty()) {
            if (sm.getMainConfig().getEquipItemMode(getEntity().getType()) == EquipItemMode.PREVENT_STACK) {
                return false;
            }
        }
        return Utilities.isPaper() ? entity.isTicking() && !getEntity().isDead() && !isMaxSize() && !isWaiting() : !getEntity().isDead() && !isMaxSize() && !isWaiting();
    }

    /**
     * Merge this stack with another stack, providing they are similar.
     *
     * @param toMerge    stack to merge with.
     * @param unregister whether to unregister the entity that is removed.
     * @return the entity that was removed
     */
    public StackEntity merge(StackEntity toMerge, boolean unregister) {
        boolean toMergeBigger = toMerge.getSize() > getSize();
        final StackEntity smallest = toMergeBigger ? this : toMerge;
        final StackEntity biggest = toMergeBigger ? toMerge : this;
        if (EventHelper.callStackMergeEvent(smallest, biggest).isCancelled()) {
            return null;
        }
        final int totalSize = smallest.getSize() + biggest.getSize();
        final int maxSize = getMaxSize();
        if (totalSize > maxSize) {
            smallest.setSize(totalSize - maxSize);
            biggest.setSize(maxSize);
            return null;
        }
        biggest.incrementSize(smallest.getSize());
        smallest.remove(unregister);
        return smallest;
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
     *
     * @param amount amount to
     * @return a clone of this entity.
     */
    public StackEntity duplicate(int amount) {
        final StackEntity cloneStack = entityManager.registerStackedEntity(spawnClone());
        cloneStack.setSize(amount);
        sm.getTraitManager().applyTraits(cloneStack, this);
        sm.getHookManager().onSpawn(cloneStack);
        return cloneStack;
    }

    private LivingEntity spawnClone() {
        final LivingEntity entity = sm.getHookManager().spawnClone(getEntity().getLocation(), this);
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
     *
     * @return stack with the remaining stack size.
     */
    public StackEntity slice() {
        return slice(1);
    }

    /**
     * Makes this stack smaller and spawns another stack with the remaining stack size.
     *
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
        final StackEntity duplicate = duplicate(getSize() - amount);
        setSize(amount);
        if (getEntity().isLeashed()) {
            duplicate.getEntity().setLeashHolder(getEntity().getLeashHolder());
            getEntity().setLeashHolder(null);
        }
        return duplicate;
    }

    public void addEquipItem(ItemStack equipt) {
        if (equiptItems == null) {
            equiptItems = new ObjectOpenHashSet<>();
        }
        equiptItems.add(equipt);
    }

    private void dropEquipItems() {
        if (equiptItems == null) {
            return;
        }
        if (sm.getMainConfig().getEquipItemMode(getEntity().getType()) != EquipItemMode.DROP_ITEMS) {
            return;
        }
        for (ItemStack itemStack : equiptItems) {
            getEntity().getWorld().dropItemNaturally(getEntity().getLocation(), itemStack);
        }
    }

    public enum EquipItemMode {
        IGNORE,
        DROP_ITEMS,
        PREVENT_STACK
    }

}
