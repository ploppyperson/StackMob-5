package uk.antiperson.stackmob.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.TagMode;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.listeners.ListenerMode;

import java.util.*;

public class MainConfig extends SpecialConfigFile {

    private final Map<EntityType, Integer> stack_max_size = new HashMap<>();
    private final Map<EntityType, Integer[]> stack_radius = new HashMap<>();
    private final List<EntityType> stack_threshold_enabled = new ArrayList<>();
    private final Map<EntityType, Integer> stack_threshold = new HashMap<>();

    private final Map<EntityType, String> tag_format = new HashMap<>();
    private final Map<EntityType, Integer> tag_threshold = new HashMap<>();
    private final Map<EntityType, TagMode> tag_tag_mode = new HashMap<>();
    private Integer[] tag_nearby_radius;

    private final List<EntityType> drop_multi = new ArrayList<>();
    private final List<EntityType> drop_loot_table = new ArrayList<>();
    private final List<EntityType> drop_type_blacklist = new ArrayList<>();
    private final Map<EntityType, List<EntityDamageEvent.DamageCause>> drop_reason_blacklist = new HashMap<>();
    private final Map<EntityType, List<Material>> drop_item_blacklist = new HashMap<>();
    private final Map<EntityType, List<Material>> drop_item_one_per = new HashMap<>();

    private final List<EntityType> exp_multi = new ArrayList<>();
    private final List<EntityType> exp_type_blacklist = new ArrayList<>();
    private final Map<EntityType, Double> exp_mult_min = new HashMap<>();
    private final Map<EntityType, Double> exp_mult_max = new HashMap<>();

    private final List<EntityType> player_stats = new ArrayList<>();

    private final List<EntityType> waiting_enabled = new ArrayList<>();
    private final List<EntityType> waiting_type_blacklist = new ArrayList<>();
    private final Map<EntityType, List<CreatureSpawnEvent.SpawnReason>> waiting_reason_blacklist = new HashMap<>();
    private final Map<EntityType, Integer> waiting_time = new HashMap<>();

    private final Map<EntityType, Integer> death_min_step = new HashMap<>();
    private final Map<EntityType, Integer> death_max_step = new HashMap<>();
    private final Map<EntityType, Map<String, List<EntityType>>> death_type_blacklist = new HashMap<>();
    private final Map<EntityType, Map<String, List<EntityDamageEvent.DamageCause>>> death_reason_blacklist = new HashMap<>();
    private final Map<EntityType, Collection<String>> death_priority = new HashMap<>();

    private final List<EntityType> targeting_disabled = new ArrayList<>();
    private final List<EntityType> targeting_type_blacklist = new ArrayList<>();
    private final Map<EntityType, List<EntityTargetEvent.TargetReason>> targeting_reason_blacklist = new HashMap<>();

    private final List<EntityType> entity_type_blacklist = new ArrayList<>();
    private final Map<EntityType, List<CreatureSpawnEvent.SpawnReason>> entity_reason_blacklist = new HashMap<>();
    private final Map<EntityType, List<World>> entity_world_blacklist = new HashMap<>();

    private final Map<EntityType, Map<String, Boolean>> remove_stack_data = new HashMap<>();
    private final Map<EntityType, Map<String, ListenerMode>> listener_mode = new HashMap<>();

    private boolean multi_slime;

    public MainConfig(StackMob sm) {
        super(sm, "config.yml");
    }

    public void cache() {
        for (EntityType type : EntityType.values()) {
            stack_max_size.put(type, getInt(type, "stack.max-size"));
            stack_radius.put(type, getList(type, "stack.merge-range").asIntList().toArray(new Integer[2]));
            if (getBoolean(type, "stack.threshold.enabled")) stack_threshold_enabled.add(type);
            stack_threshold.put(type, getInt(type, "stack.threshold.amount"));

            tag_format.put(type, getString(type, "display-name.format"));
            tag_threshold.put(type, getInt(type, "display-name.threshold"));
            tag_tag_mode.put(type, TagMode.valueOf(getString(type, "display-name.visibility")));

            if (getBoolean(type, "drops.enabled")) drop_multi.add(type);
            if (getBoolean(type, "drops.use-loot-tables")) drop_loot_table.add(type);
            if (getList("drops.type-blacklist").contains(type.toString())) drop_type_blacklist.add(type);
            List<EntityDamageEvent.DamageCause> dropCauseList = new ArrayList<>();
            for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
                if (getList(type, "drops.reason-blacklist").contains(damageCause.toString()))
                    dropCauseList.add(damageCause);
            }
            drop_reason_blacklist.put(type, dropCauseList);
            List<Material> materialList = new ArrayList<>();
            List<Material> oneMaterialList = new ArrayList<>();
            for (Material material : Material.values()) {
                if (getList(type, "drops.item-blacklist").contains(material.toString())) materialList.add(material);
                if (getList(type, "drops.one-per-stack").contains(material.toString())) oneMaterialList.add(material);
            }
            drop_item_blacklist.put(type, materialList);
            drop_item_one_per.put(type, oneMaterialList);

            if (getBoolean(type, "experience.enabled")) exp_multi.add(type);
            if (getList(type, "experience.type-blacklist").contains(type.toString())) exp_type_blacklist.add(type);
            exp_mult_min.put(type, getDouble(type, "experience.multiplier-min"));
            exp_mult_max.put(type, getDouble(type, "experience.multiplier-max"));

            if (getBoolean(type, "player-stats")) player_stats.add(type);

            if (getBoolean(type, "wait-to-stack.enabled")) waiting_enabled.add(type);
            if (getList("wait-to-stack.types-whitelist").contains(type.toString())) waiting_type_blacklist.add(type);
            waiting_time.put(type, getInt(type, "wait-to-stack.wait-time"));

            death_min_step.put(type, getInt(type, "death.STEP.min-step"));
            death_max_step.put(type, getInt(type, "death.STEP.max-step"));
            Collection<String> priorities = getDeathSection(type);
            death_priority.put(type, priorities);
            for (String key : priorities) {
                Map<String, List<EntityType>> deathTypeMap = death_type_blacklist.getOrDefault(type, new HashMap<>());
                List<EntityType> deathTypeList = new ArrayList<>();
                for (EntityType entityType : EntityType.values()) {
                    if (getList(type, "death." + key + ".type-blacklist").contains(entityType.toString()))
                        deathTypeList.add(entityType);
                }
                deathTypeMap.put(key, deathTypeList);
                death_type_blacklist.put(type, deathTypeMap);

                Map<String, List<EntityDamageEvent.DamageCause>> deathCauseMap = death_reason_blacklist.getOrDefault(type, new HashMap<>());
                List<EntityDamageEvent.DamageCause> deathCauseList = new ArrayList<>();
                for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
                    if (getList(type, "death." + key + ".reason-blacklist").contains(damageCause.toString()))
                        deathCauseList.add(damageCause);
                }
                deathCauseMap.put(key, deathCauseList);
                death_reason_blacklist.put(type, deathCauseMap);
            }

            if (getBoolean(type, "disable-targeting.enabled")) targeting_disabled.add(type);
            if (getList(type, "disable-targeting.type-blacklist").contains(type.toString()))
                targeting_type_blacklist.add(type);
            List<EntityTargetEvent.TargetReason> targetReasonList = new ArrayList<>();
            for (EntityTargetEvent.TargetReason targetReason : EntityTargetEvent.TargetReason.values()) {
                if (getList(type, "disable-targeting.reason-blacklist").contains(targetReason.toString()))
                    targetReasonList.add(targetReason);
            }
            targeting_reason_blacklist.put(type, targetReasonList);

            if (getList(type, "types-blacklist").contains(type.toString())) entity_type_blacklist.add(type);
            List<CreatureSpawnEvent.SpawnReason> stackReasonList = new ArrayList<>();
            List<CreatureSpawnEvent.SpawnReason> waitReasonList = new ArrayList<>();
            for (CreatureSpawnEvent.SpawnReason spawnReason : CreatureSpawnEvent.SpawnReason.values()) {
                if (getList(type, "reason-blacklist").contains(spawnReason.toString()))
                    stackReasonList.add(spawnReason);
                if (getList(type, "wait-to-stack.reasons-whitelist").contains(spawnReason.toString()))
                    waitReasonList.add(spawnReason);
            }
            entity_reason_blacklist.put(type, stackReasonList);
            waiting_reason_blacklist.put(type, waitReasonList);
            List<World> worldList = new ArrayList<>();
            for (World world : Bukkit.getWorlds()) {
                if (getList(type, "worlds-blacklist").contains(world.getName())) worldList.add(world);
            }
            entity_world_blacklist.put(type, worldList);

            Map<String, Boolean> removeStackDataMap = remove_stack_data.getOrDefault(type, new HashMap<>());
            for (String key : getConfigurationSection("events.remove-stack-data").getKeys(false)) {
                removeStackDataMap.put(key, getBoolean(type, "events.remove-stack-data." + key));
            }
            remove_stack_data.put(type, removeStackDataMap);
            Map<String, ListenerMode> listenerModeMap = listener_mode.getOrDefault(type, new HashMap<>());
            for (String key : getConfigurationSection("events").getKeys(false)) {
                String mode = getString(type, "events." + key + ".mode");
                if (mode != null) {
                    listenerModeMap.put(key, ListenerMode.valueOf(mode));
                }
            }
            listener_mode.put(type, listenerModeMap);
        }
        tag_nearby_radius = getList("display-name.nearby.range").asIntList().toArray(new Integer[2]);

        multi_slime = getBoolean("events.multiply.slime-split");
    }

    public int getMaxStack(EntityType type) {
        return stack_max_size.get(type);
    }

    public boolean getStackThresholdEnabled(EntityType type) {
        return stack_threshold_enabled.contains(type);
    }

    public int getStackThreshold(EntityType type) {
        return stack_threshold.get(type);
    }

    public Integer[] getStackRadius(EntityType type) {
        return stack_radius.get(type);
    }

    public int getStackInterval() {
        return getInt("stack.interval");
    }

    public String getTagFormat(EntityType type) {
        return tag_format.get(type);
    }

    public int getTagThreshold(EntityType type) {
        return tag_threshold.get(type);
    }

    public TagMode getTagMode(EntityType type) {
        return tag_tag_mode.get(type);
    }

    public Integer[] getTagNearbyRadius() {
        return tag_nearby_radius;
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
        return drop_multi.contains(type);
    }

    public boolean isDropLootTables(EntityType type) {
        return drop_loot_table.contains(type);
    }

    public boolean isSlimeMultiEnabled() {
        return multi_slime;
    }

    public boolean isDropTypeBlacklisted(EntityType type) {
        return drop_type_blacklist.contains(type);
    }

    public boolean isDropReasonBlacklisted(EntityType type, EntityDamageEvent.DamageCause cause) {
        return drop_reason_blacklist.get(type).contains(cause);
    }

    public boolean idDropItemBlacklisted(EntityType type, Material material) {
        return drop_item_blacklist.get(type).contains(material);
    }

    public boolean isDropItemOnePer(EntityType type, Material material) {
        return drop_item_one_per.get(type).contains(material);
    }

    public boolean isExpMultiEnabled(EntityType type) {
        return exp_multi.contains(type);
    }

    public boolean isExpTypeBlacklisted(EntityType type) {
        return exp_type_blacklist.contains(type);
    }

    public double getExpMinBound(EntityType type) {
        return exp_mult_min.get(type);
    }

    public double getExpMaxBound(EntityType type) {
        return exp_mult_max.get(type);
    }

    public boolean isPlayerStatMulti(EntityType type) {
        return player_stats.contains(type);
    }

    public boolean isWaitingEnabled(EntityType type) {
        return waiting_enabled.contains(type);
    }

    public boolean isWaitingType(EntityType type) {
        return waiting_type_blacklist.contains(type);
    }

    public boolean isWaitingReason(EntityType type, CreatureSpawnEvent.SpawnReason reason) {
        return waiting_reason_blacklist.get(type).contains(reason);
    }

    public int getWaitingTime(EntityType type) {
        return waiting_time.get(type);
    }

    public int getMaxDeathStep(EntityType type) {
        return death_max_step.get(type);
    }

    public int getMinDeathStep(EntityType type) {
        return death_min_step.get(type);
    }

    public boolean removeStackDataOnDivide(EntityType type, String reasonKey) {
        return remove_stack_data.get(type).get(reasonKey);
    }

    public boolean isTargetingDisabled(EntityType type) {
        return targeting_disabled.contains(type);
    }

    public boolean isTargetingDisabledType(EntityType type) {
        return targeting_type_blacklist.contains(type);
    }

    public boolean isTargetingDisabledReason(EntityType type, EntityTargetEvent.TargetReason reason) {
        return targeting_reason_blacklist.get(type).contains(reason);
    }

    public ListenerMode getListenerMode(EntityType type, String eventKey) {
        return listener_mode.get(type).get(eventKey);
    }

    public boolean isEntityBlacklisted(LivingEntity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (entity_type_blacklist.contains(entity.getType())) {
            return true;
        }
        if (entity_reason_blacklist.get(entity.getType()).contains(reason)) {
            return true;
        }
        return entity_world_blacklist.get(entity.getType()).contains(entity.getWorld());
    }

    public DeathType getDeathType(LivingEntity entity) {
        for (String key : death_priority.get(entity.getType())) {
            if (death_reason_blacklist.get(entity.getType()).get(key).contains(entity.getLastDamageCause().getCause())) {
                continue;
            }
            if (death_type_blacklist.get(entity.getType()).get(key).contains(entity.getType())) {
                continue;
            }
            return DeathType.valueOf(key);
        }
        throw new UnsupportedOperationException("Configuration error - unable to determine death type!");
    }

    private Collection<String> getDeathSection(EntityType type) {
        TreeMap<Integer, String> array = new TreeMap<>();
        for (String key : getConfigurationSection("death").getKeys(false)) {
            array.put(getInt(type, "death." + key + ".priority"), key);
        }
        return array.values();
    }

}
