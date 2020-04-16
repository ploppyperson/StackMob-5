package uk.antiperson.stackmob.listeners;

import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.material.Wool;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.Drops;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

@ListenerMetadata(config = "events.shear.enabled")
public class ShearListener implements Listener {

    private StackMob sm;

    public ShearListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onShearSheep(PlayerShearEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        ItemStack is = shearLogic((LivingEntity) event.getEntity(), event.getPlayer().getInventory().getItemInMainHand());
        if (is == null) {
            return;
        }
        event.getPlayer().getInventory().setItemInMainHand(is);
    }

    @EventHandler
    public void onShearSheep(BlockShearEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        ItemStack is = shearLogic((LivingEntity) event.getEntity(), event.getTool());
        if (is == null) {
            return;
        }
        sm.getServer().getScheduler().runTaskLater(sm, () -> {
            Dispenser dispenser = (Dispenser) event.getBlock().getState();
            dispenser.getInventory().setItem(dispenser.getInventory().first(event.getTool()), is);
        }, 1);
    }

    private ItemStack shearLogic(LivingEntity entity, ItemStack item) {
        if (!((entity instanceof Sheep) || (entity instanceof MushroomCow))) {
            return null;
        }
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(entity);
        if (stackEntity.isSingle()) {
            return null;
        }
        switch (sm.getMainConfig().getListenerMode(entity.getType(), "shear")) {
            case SPLIT:
                StackEntity slice = stackEntity.slice();
                if (slice.getEntity() instanceof Sheep) {
                    ((Sheep) slice.getEntity()).setSheared(false);
                }
                break;
            case MULTIPLY:
                Damageable damageable = (Damageable) item.getItemMeta();
                int health = item.getType().getMaxDurability() - damageable.getDamage();
                stackEntity.splitIfNotEnough(health);
                int damage = health - stackEntity.getSize();
                if (damage > 0) {
                    damageable.setDamage(damageable.getDamage() + stackEntity.getSize());
                    item.setItemMeta((ItemMeta) damageable);
                } else {
                    item = new ItemStack(Material.AIR);
                }
                if (entity instanceof Sheep) {
                    Sheep sheared = (Sheep) entity;
                    LootContext lootContext = new LootContext.Builder(sheared.getLocation()).lootedEntity(sheared).build();
                    Collection<ItemStack> loot = sheared.getLootTable().populateLoot(ThreadLocalRandom.current(), lootContext);
                    for(ItemStack itemStack : loot){
                        if(itemStack.getData() instanceof Wool) {
                            int woolAmount = (int) Math.round(stackEntity.getSize() * ThreadLocalRandom.current().nextDouble(1,2));
                            Drops.dropItem(sheared.getLocation(), itemStack, woolAmount);
                        }
                    }
                    return item;
                }
                MushroomCow mushroomCow = (MushroomCow) entity;
                ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM,1);
                Drops.dropItem(mushroomCow.getLocation(), mushrooms, (stackEntity.getSize() - 1) * 5);
                // Spawn separate normal cow for the rest of the stack.
                Entity cow = mushroomCow.getWorld().spawnEntity(mushroomCow.getLocation(), EntityType.COW);
                StackEntity stackCow = sm.getEntityManager().getStackEntity((LivingEntity) cow);
                stackCow.setSize(stackEntity.getSize() - 1);
                return item;
        }
        return null;
    }

}
