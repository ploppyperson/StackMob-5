package uk.antiperson.stackmob.config;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.entity.TagMode;
import uk.antiperson.stackmob.listeners.ListenerMode;

import java.util.*;

public class MainConfig extends ConfigFile{

    public MainConfig(StackMob sm) {
        super(sm, "config.yml");
    }

    public int getMaxStack(EntityType type) {
        return getInt(type, "stack.max-size");
    }

    public boolean getStackThresholdEnabled(EntityType type) {
        return getBoolean(type, "stack.threshold.enabled");
    }

    public int getStackThreshold(EntityType type) {
        return getInt(type, "stack.threshold.amount");
    }

    public Integer[] getStackRadius(EntityType type) {
        return getList(type, "stack.merge-range").asIntList().toArray(new Integer[2]);
    }

    public int getStackInterval() {
        return getInt("stack.interval");
    }

    public String getTagFormat(EntityType type) {
        return getString(type, "display-name.format");
    }

    public int getTagThreshold(EntityType type) {
        return getInt(type, "display-name.threshold");
    }

    public TagMode getTagMode(EntityType type) {
        return TagMode.valueOf(getString(type, "display-name.visibility"));
    }

    public Integer[] getTagNeabyRadius() {
        return getList("display-name.nearby.range").asIntList().toArray(new Integer[2]);
    }

    public int getTagNearbyInterval() {
        return getInt("display-name.nearby.interval");
    }

    public boolean isTraitEnabled(String traitKey) {
        return getBoolean("traits." + traitKey);
    }

    public boolean isHookEnabled(String traitKey) {
        return getBoolean("hooks." + traitKey);
    }

    public boolean isDropMultiEnabled(EntityType type) {
        return getBoolean(type, "drops.enabled");
    }

    public boolean isDropLootTables(EntityType type) {
        return getBoolean(type, "drops.use-loot-tables");
    }

    public ConfigList getDropTypeBlacklist(EntityType type) {
        return getList(type, "drops.type-blacklist");
    }

    public ConfigList getDropReasonBlacklist(EntityType type) {
        return getList(type, "drops.reason-blacklist");
    }

    public ConfigList getDropItemBlacklist(EntityType type) {
        return getList(type, "drops.item-blacklist");
    }

    public ConfigList getDropItemOnePer(EntityType type) {
        return getList(type, "drops.one-per-stack");
    }

    public boolean isExpMultiEnabled(EntityType type) {
        return getBoolean(type, "experience.enabled");
    }

    public ConfigList getExpTypeBlacklist(EntityType type) {
        return getList(type, "experience.type-blacklist");
    }

    public double getExpMinBound(EntityType type) {
        return getDouble(type, "experience.multiplier-min");
    }

    public double getExpMaxBound(EntityType type) {
        return getDouble(type, "experience.multiplier-max");
    }

    public boolean isWaitingEnabled(EntityType type) {
        return getBoolean(type, "wait-to-stack.enabled");
    }

    public ConfigList getWaitingTypes(EntityType type) {
        return getList(type, "wait-to-stack.types-whitelist");
    }

    public ConfigList getWaitingReasons(EntityType type) {
        return getList(type, "wait-to-stack.reasons-whitelist");
    }

    public int getWaitingTime(EntityType type) {
        return getInt(type, "wait-to-stack.wait-time");
    }

    public int getMaxDeathStep(EntityType type) {
        return getInt(type, "death.STEP.max-step");
    }

    public int getMinDeathStep(EntityType type) {
        return getInt(type, "death.STEP.min-step");
    }

    public boolean isListenerEnabled(String eventKey) {
        return getBoolean("events." + eventKey);
    }

    public boolean isAiDisabled(EntityType type, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (getList(type, "disable-ai.type-blacklist").contains(type.toString())) {
            return false;
        }
        if (getList(type, "disable-ai.reason-blacklist").contains(spawnReason.toString())) {
            return false;
        }
        return getBoolean(type, "disable-ai.enabled");
    }

    public ListenerMode getListenerMode(EntityType type, String eventKey) {
        return ListenerMode.valueOf(getString(type, "events." + eventKey + ".mode"));
    }

    public boolean isWorldBlacklisted(EntityType type, World world) {
        return getList(type, "worlds-blacklist").contains(world.getName());
    }

    public boolean isEntityBlacklisted(LivingEntity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (getList(entity.getType(), "types-blacklist").contains(entity.getType().toString())) {
            return true;
        }
        if (getList(entity.getType(), "reason-blacklist").contains(reason.toString())) {
            return true;
        }
        return isWorldBlacklisted(entity.getType(), entity.getWorld());
    }

    public DeathType getDeathType(LivingEntity dead) {
        for (String key : getDeathSection(dead)) {
            ConfigList reasons = getList(dead.getType(), "death." + key + ".reason-blacklist");
            if (reasons.contains(dead.getLastDamageCause().getCause().toString())) {
                continue;
            }
            ConfigList types = getList(dead.getType(), "death." + key + ".type-blacklist");
            if (types.contains(dead.getType().toString())) {
                continue;
            }
            return DeathType.valueOf(key);
        }
        throw new UnsupportedOperationException("Configuration error - unable to determine death type!");
    }

    private Collection<String> getDeathSection(LivingEntity dead) {
        TreeMap<Integer, String> array = new TreeMap<>();
        for (String key : getConfigurationSection("death").getKeys(false)) {
            array.put(getInt(dead.getType(), "death." + key + ".priority"), key);
        }
        return array.values();
    }

}
