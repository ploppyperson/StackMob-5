package uk.antiperson.stackmob.config;

import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Raider;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.listeners.ListenerMode;
import uk.antiperson.stackmob.utils.Utilities;

import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;

public class MainConfig extends SpecialConfigFile {

    private final StackMob sm;
    public MainConfig(StackMob sm) {
        super(sm, "config.yml");
        this.sm = sm;
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

    public boolean isCheckHasMoved() {
        return getBoolean("stack.check-location.enabled");
    }

    public double getCheckHasMovedDistance() {
        return getDouble("stack.check-location.distance");
    }

    public String getTagFormat(EntityType type) {
        return getString(type, "display-name.format");
    }

    public int getTagThreshold(EntityType type) {
        return getInt(type, "display-name.threshold");
    }

    public StackEntity.TagMode getTagMode(EntityType type) {
        return StackEntity.TagMode.valueOf(getString(type, "display-name.visibility"));
    }

    public Integer[] getTagNeabyRadius() {
        return getList("display-name.nearby.range").asIntList().toArray(new Integer[2]);
    }

    public int getTagNearbyInterval() {
        return getInt("display-name.nearby.interval");
    }

    public boolean isTagNearbyRayTrace() {
        return getBoolean("display-name.nearby.ray-trace");
    }

    public boolean isUseArmorStand() {
        return getBoolean("display-name.nearby.use-armorstand");
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

    public boolean isSlimeMultiEnabled(EntityType type) {
        return getBoolean(type, "multiply.slime-split");
    }

    public boolean isDropTypeBlacklist(EntityType type) {
        return isEntityTypeInList(type, "drops.type-blacklist");
    }

    public boolean isDropReasonBlacklist(EntityType type, EntityDamageEvent.DamageCause damageCause) {
        return getList(type, "drops.reason-blacklist").contains(damageCause.toString());
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

    public boolean isExpTypeBlacklist(EntityType type) {
        return getList(type, "experience.type-blacklist").contains(type.toString());
    }

    public double getExpMinBound(EntityType type) {
        return getDouble(type, "experience.multiplier-min");
    }

    public double getExpMaxBound(EntityType type) {
        return getDouble(type, "experience.multiplier-max");
    }

    public boolean isPlayerStatMulti(EntityType type) {
        return getBoolean(type, "player-stats");
    }

    public boolean isWaitingEnabled(EntityType type) {
        return getBoolean(type, "wait-to-stack.enabled");
    }

    public boolean isWaitingTypes(EntityType type) {
        return isEntityTypeInList(type, "wait-to-stack.types-whitelist");
    }

    public boolean isWaitingReasons(EntityType type, CreatureSpawnEvent.SpawnReason spawnReason) {
        return getList(type, "wait-to-stack.reasons-whitelist").contains(spawnReason.toString());
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

    public boolean removeStackDataOnDivide(String reasonKey) { return getBoolean("events.remove-stack-data." + reasonKey); }

    public boolean isTargetingDisabled(EntityType type) {
        return getBoolean(type, "disable-targeting.enabled");
    }

    public boolean isTargetingDisabledTypes(EntityType type) {
        return isEntityTypeInList(type, "disable-targeting.type-blacklist");
    }

    public boolean isTargetingDisabledReasons(EntityType type, CreatureSpawnEvent.SpawnReason spawnReason) {
        return getList(type, "disable-targeting.reason-blacklist").contains(spawnReason.toString());
    }

    public ListenerMode getListenerMode(EntityType type, String eventKey) {
        return ListenerMode.valueOf(getString(type, "events." + eventKey + ".mode"));
    }

    public int getEventMultiplyLimit(EntityType type, String eventKey, int stackSize) {
        int limit = getInt(type, "events." + eventKey + ".limit");
        return limit == -1 ? stackSize : Math.min(stackSize, limit);
    }

    public boolean isWorldBlacklisted(EntityType type, World world) {
        return getList(type, "worlds-blacklist").contains(world.getName());
    }

    public boolean isEntityBlacklisted(LivingEntity entity) {
        CreatureSpawnEvent.SpawnReason reason = Utilities.isPaper() ? entity.getEntitySpawnReason() : CreatureSpawnEvent.SpawnReason.DEFAULT;
        return isEntityBlacklisted(entity, reason);
    }

    public boolean isEntityBlacklisted(LivingEntity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (isEntityTypeInList(entity.getType(), "types-blacklist")) {
            return true;
        }
        if (getList(entity.getType(), "reason-blacklist").contains(reason.toString())) {
            return true;
        }
        return isWorldBlacklisted(entity.getType(), entity.getWorld());
    }

    public DeathType getDeathType(LivingEntity dead) {
        for (DeathType type : getDeathSection(dead)) {
            ConfigList reasons = getList(dead.getType(), "death." + type + ".reason-blacklist");
            if (dead.getLastDamageCause() != null && reasons.contains(dead.getLastDamageCause().getCause().toString())) {
                continue;
            }
            ConfigList spawnReasons = getList(dead.getType(), "death." + type + ".spawn-reason-blacklist");
            if (Utilities.isPaper() && spawnReasons.contains(dead.getEntitySpawnReason().toString())) {
                continue;
            }
            if (isEntityTypeInList(dead.getType(), "death." + type + ".type-blacklist")) {
                continue;
            }
            return type;
        }
        throw new UnsupportedOperationException("Configuration error - unable to determine death type!");
    }

    private Collection<DeathType> getDeathSection(LivingEntity dead) {
        TreeMap<Integer, DeathType> array = new TreeMap<>();
        for (DeathType type : DeathType.values()) {
            array.put(getInt(dead.getType(), "death." + type + ".priority"), type);
        }
        return array.values();
    }

    public boolean isSkipDeathAnimation(EntityType type) {
        return getBoolean(type, "death.skip-animation") && Utilities.isPaper();
    }

    public StackEntity.EquipItemMode getEquipItemMode(EntityType type) {
        return StackEntity.EquipItemMode.valueOf(getString(type, "events.equip.mode"));
    }

    public boolean isStackOnSpawn() {
        return getBoolean("stack.on-spawn");
    }

    private boolean isEntityTypeInList(EntityType type, String path) {
        ConfigList list = getList(type, path);
        if (list.isInverted() && list.rawContains(type.toString())) {
            return false;
        }
        for (EntityGrouping entityGrouping : EntityGrouping.values()) {
            if (!list.rawContains(entityGrouping.toString())) {
                continue;
            }
            if (entityGrouping.isEntityMemberOf(type.getEntityClass())) {
                return !list.isInverted();
            }
        }
        return list.contains(type.toString());
    }

    @Override
    public void updateFile() throws IOException {
        if (isSet("check-area.x")) {
            sm.getLogger().info("Old config detected. Renaming to config.old and making a new one.");
            makeOld();
            sm.downloadBridge();
            return;
        }
        super.updateFile();
    }

    enum EntityGrouping {
        HOSTILE(Monster.class, Ghast.class, Phantom.class),
        ANIMALS(Animals.class),
        WATER(WaterMob.class),
        RAIDER(Raider.class),
        BOSS(Boss.class);

        Class<? extends Entity>[] classes;
        EntityGrouping(Class<? extends Entity>... classes) {
            this.classes = classes;
        }

        public boolean isEntityMemberOf(Class<? extends Entity> entity) {
            if (entity == null) {
                return false;
            }
            for (Class<? extends Entity> entityClass : classes) {
                if (entityClass.isAssignableFrom(entity)) {
                    return true;
                }
            }
            return false;
        }
    }

}
