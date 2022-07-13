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
import uk.antiperson.stackmob.utils.Utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EntityConfig {

    private final Map<String, ConfigValue> map;
    private final EntityType type;

    public EntityConfig(EntityType type) {
        this.type = type;
        this.map = new HashMap<>();
    }

    public void put(String path, ConfigValue value) {
        map.put(path, value);
    }

    public int getMaxStack() {
        return map.get("stack.max-size").getInt();
    }

    public boolean getStackThresholdEnabled() {
        return map.get( "stack.threshold.enabled").getBoolean();
    }

    public int getStackThreshold() {
        return map.get( "stack.threshold.amount").getInt();
    }

    public Integer[] getStackRadius() {
        return map.get("stack.merge-range").asIntList().toArray(new Integer[2]);
    }

    public int getStackInterval() {
        return map.get("stack.interval").getInt();
    }

    public boolean isCheckHasMoved() {
        return map.get("stack.check-location.enabled").getBoolean();
    }

    public double getCheckHasMovedDistance() {
        return map.get("stack.check-location.distance").getDouble();
    }

    public String getTagFormat() {
        return map.get("display-name.format").getString();
    }

    public int getTagThreshold() {
        return map.get( "display-name.threshold").getInt();
    }

    public StackEntity.TagMode getTagMode() {
        return StackEntity.TagMode.valueOf(map.get("display-name.visibility").getString());
    }

    public Integer[] getTagNeabyRadius() {
        return map.get("display-name.nearby.range").asIntList().toArray(new Integer[2]);
    }

    public int getTagNearbyInterval() {
        return map.get("display-name.nearby.interval").getInt();
    }

    public boolean isTagNearbyRayTrace() {
        return map.get("display-name.nearby.ray-trace").getBoolean();
    }

    public boolean isUseArmorStand() {
        return map.get("display-name.nearby.use-armorstand").getBoolean();
    }

    public boolean isTraitEnabled(String traitKey) {
        return map.get("traits." + traitKey).getBoolean();
    }

    public boolean isHookEnabled(String traitKey) {
        return map.get("hooks." + traitKey).getBoolean();
    }

    public boolean isDropMultiEnabled() {
        return map.get( "drops.enabled").getBoolean();
    }

    public boolean isDropLootTables() {
        return map.get( "drops.use-loot-tables").getBoolean();
    }

    public boolean isSlimeMultiEnabled() {
        return map.get( "multiply.slime-split").getBoolean();
    }

    public boolean isDropTypeBlacklist() {
        return isEntityTypeInList("drops.type-blacklist");
    }

    public boolean isDropReasonBlacklist(EntityDamageEvent.DamageCause damageCause) {
        return map.get("drops.reason-blacklist").getList().contains(damageCause.toString());
    }

    public ConfigList getDropItemBlacklist() {
        return map.get("drops.item-blacklist").getList();
    }

    public ConfigList getDropItemOnePer() {
        return map.get("drops.one-per-stack").getList();
    }

    public boolean isExpMultiEnabled() {
        return map.get( "experience.enabled").getBoolean();
    }

    public boolean isExpTypeBlacklist() {
        return map.get("experience.type-blacklist").getList().contains(type.toString());
    }

    public double getExpMinBound() {
        return map.get("experience.multiplier-min").getDouble();
    }

    public double getExpMaxBound() {
        return map.get("experience.multiplier-max").getDouble();
    }

    public boolean isPlayerStatMulti() {
        return map.get( "player-stats").getBoolean();
    }

    public boolean isWaitingEnabled() {
        return map.get( "wait-to-stack.enabled").getBoolean();
    }

    public boolean isWaitingTypes() {
        return isEntityTypeInList("wait-to-stack.types-whitelist");
    }

    public boolean isWaitingReasons(CreatureSpawnEvent.SpawnReason spawnReason) {
        return map.get("wait-to-stack.reasons-whitelist").getList().contains(spawnReason.toString());
    }

    public int getWaitingTime() {
        return map.get("wait-to-stack.wait-time").getInt();
    }

    public int getMaxDeathStep() {
        return map.get( "death.STEP.max-step").getInt();
    }

    public int getMinDeathStep() {
        return map.get( "death.STEP.min-step").getInt();
    }

    public boolean isListenerEnabled(String eventKey) {
        return map.get("events." + eventKey).getBoolean();
    }

    public boolean removeStackDataOnDivide(String reasonKey) { return map.get("events.remove-stack-data." + reasonKey).getBoolean(); }

    public boolean isTargetingDisabled() {
        return map.get( "disable-targeting.enabled").getBoolean();
    }

    public boolean isTargetingDisabledTypes() {
        return isEntityTypeInList("disable-targeting.type-blacklist");
    }

    public boolean isTargetingDisabledReasons(CreatureSpawnEvent.SpawnReason spawnReason) {
        return map.get("disable-targeting.reason-blacklist").getList().contains(spawnReason.toString());
    }

    public ListenerMode getListenerMode(EventType eventType) {
        return ListenerMode.valueOf(map.get("events." + eventType.getConfigKey() + ".mode").getString());
    }

    public int getEventMultiplyLimit(EventType eventType, int stackSize) {
        int limit = map.get( "events." + eventType.getConfigKey() + ".limit").getInt();
        return limit == -1 ? stackSize : Math.min(stackSize, limit);
    }

    public boolean isWorldBlacklisted(World world) {
        return map.get("worlds-blacklist").getList().contains(world.getName());
    }

    public boolean isEntityBlacklisted(LivingEntity entity) {
        CreatureSpawnEvent.SpawnReason reason = Utilities.isPaper() ? entity.getEntitySpawnReason() : CreatureSpawnEvent.SpawnReason.DEFAULT;
        return isEntityBlacklisted(entity, reason);
    }

    public boolean isEntityBlacklisted(LivingEntity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (isEntityTypeInList("types-blacklist")) {
            return true;
        }
        if (map.get("reason-blacklist").getList().contains(reason.toString())) {
            return true;
        }
        return isWorldBlacklisted(entity.getWorld());
    }

    public DeathType getDeathType(LivingEntity dead) {
        for (DeathType type : getDeathSection()) {
            ConfigList reasons = map.get("death." + type + ".reason-blacklist").getList();
            if (dead.getLastDamageCause() != null && reasons.contains(dead.getLastDamageCause().getCause().toString())) {
                continue;
            }
            ConfigList spawnReasons = map.get("death." + type + ".spawn-reason-blacklist").getList();
            if (Utilities.isPaper() && spawnReasons.contains(dead.getEntitySpawnReason().toString())) {
                continue;
            }
            if (isEntityTypeInList("death." + type + ".type-blacklist")) {
                continue;
            }
            return type;
        }
        throw new UnsupportedOperationException("Configuration error - unable to determine death type!");
    }

    private Collection<DeathType> getDeathSection() {
        TreeMap<Integer, DeathType> array = new TreeMap<>();
        for (DeathType type : DeathType.values()) {
            array.put(map.get("death." + type + ".priority").getInt(), type);
        }
        return array.values();
    }

    public boolean isSkipDeathAnimation() {
        return map.get( "death.skip-animation").getBoolean() && Utilities.isPaper();
    }

    public StackEntity.EquipItemMode getEquipItemMode() {
        return StackEntity.EquipItemMode.valueOf(map.get("events.equip.mode").getString());
    }

    public boolean isStackOnSpawn() {
        return map.get("stack.on-spawn").getBoolean();
    }

    private boolean isEntityTypeInList(String path) {
        ConfigList list = map.get(path).getList();
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

    public EntityType getType() {
        return type;
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

    public enum EventType {
        BREED("breed"),
        DYE("dye"),
        EQUIP("equip"),
        EXPLOSION("explosion"),
        SHEAR("shear");

        String configKey;
        EventType(String configKey) {
            this.configKey = configKey;
        }

        public String getConfigKey() {
            return configKey;
        }
    }

    public enum ListenerMode {
        MULTIPLY,
        SPLIT
    }
}
