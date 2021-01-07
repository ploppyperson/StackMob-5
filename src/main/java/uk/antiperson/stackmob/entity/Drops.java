package uk.antiperson.stackmob.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Drops {

    private final LivingEntity dead;
    private final StackMob sm;

    public Drops(StackMob sm, StackEntity entity) {
        this.sm = sm;
        this.dead = entity.getEntity();
    }

    public Map<ItemStack, Integer> calculateDrops(int deathAmount, List<ItemStack> originalDrops) {
        Map<ItemStack, Integer> items = new Object2IntOpenHashMap<>();
        if (!sm.getMainConfig().isDropMultiEnabled(dead.getType())) {
            return items;
        }
        if (sm.getMainConfig().isDropTypeBlacklisted(dead.getType())) {
            return items;
        }
        EntityDamageEvent lastDamageCause = dead.getLastDamageCause();
        if (lastDamageCause == null || sm.getMainConfig().isDropReasonBlacklisted(dead.getType(), lastDamageCause.getCause())) {
            return items;
        }
        for (int i = 0; i < deathAmount; i++) {
            for (ItemStack is : calculateLoot(originalDrops)) {
                if (is == null || is.getType() == Material.AIR) {
                    continue;
                }
                if (dropIsArmor(is)) {
                    continue;
                }
                if (is.getType() == Material.LEAD && dead.isLeashed()) {
                    continue;
                }
                if (sm.getMainConfig().isDropItemBlacklisted(dead.getType(), is.getType())) {
                    continue;
                }
                int dropAmount = is.getAmount();
                if (sm.getMainConfig().isDropItemOnePer(dead.getType(), is.getType())) {
                    dropAmount = 1;
                }
                is.setAmount(1);
                if (items.containsKey(is)) {
                    items.put(is, items.get(is) + dropAmount);
                    continue;
                }
                items.put(is, dropAmount);
            }
        }
        return items;
    }

    private Collection<ItemStack> calculateLoot(List<ItemStack> originalDrops) {
        if (!sm.getMainConfig().isDropLootTables(dead.getType())) {
            return originalDrops;
        }
        LootContext lc = new LootContext.Builder(dead.getLocation()).lootedEntity(dead).killer(dead.getKiller()).build();
        return ((Mob) dead).getLootTable().populateLoot(ThreadLocalRandom.current(), lc);
    }

    public static void dropItems(Location location, Map<ItemStack, Integer> items) {
        for (Map.Entry<ItemStack, Integer> entry : items.entrySet())
            dropItem(location, entry.getKey(), entry.getValue());
    }

    public static void dropItem(Location location, ItemStack stack, int amount) {
        for (int itemAmount : Utilities.split(amount, stack.getMaxStackSize())) {
            stack.setAmount(itemAmount);
            location.getWorld().dropItemNaturally(location, stack);
        }
    }

    private boolean dropIsArmor(ItemStack stack) {
        if (dead.getEquipment().getItemInMainHand().equals(stack) || dead.getEquipment().getItemInOffHand().equals(stack)) {
            return true;
        }
        for (ItemStack itemStack : dead.getEquipment().getArmorContents()) {
            if (itemStack.equals(stack)) {
                return true;
            }
        }
        return false;
    }

    public int calculateDeathExperience(int deadCount, int exp) {
        if (!sm.getMainConfig().isExpMultiEnabled(dead.getType())) {
            return exp;
        }
        if (sm.getMainConfig().isExpTypeBlacklisted(dead.getType())) {
            return exp;
        }
        double minMulti = sm.getMainConfig().getExpMinBound(dead.getType()) * exp;
        double maxMulti = sm.getMainConfig().getExpMaxBound(dead.getType()) * exp;
        return exp + calculateExperience(minMulti, maxMulti, deadCount);
    }

    private int calculateExperience(double min, double max, int entities) {
        double randRange = min == max ? max : ThreadLocalRandom.current().nextDouble(min, max);
        double randMultiplier = ThreadLocalRandom.current().nextDouble(0.5, 1);
        return (int) Math.round(randRange * randMultiplier * entities);
    }

    public void dropExperience(Location location, int min, int max, int entities) {
        ExperienceOrb exp = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        exp.setExperience(calculateExperience(min, max, entities));
    }

}
