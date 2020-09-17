package uk.antiperson.stackmob.config;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.TagMode;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.listeners.ListenerMode;

import java.io.IOException;
import java.util.*;

public class MainConfig extends SpecialConfigFile {

    private int default_stack_max_size;
    private final Map<EntityType, Integer> stack_max_size = new EnumMap<>(EntityType.class);
    private Integer[] default_stack_merge_range;
    private final Map<EntityType, Integer[]> stack_merge_range = new EnumMap<>(EntityType.class);
    private boolean default_stack_threshold_enabled;
    private final Map<EntityType, Boolean> stack_threshold_enabled = new EnumMap<>(EntityType.class);
    private int default_stack_threshold_amount;
    private final Map<EntityType, Integer> stack_threshold_amount = new EnumMap<>(EntityType.class);


    private String default_display_name_format;
    private final Map<EntityType, String> display_name_format = new EnumMap<>(EntityType.class);
    private TagMode default_display_name_visibility;
    private final Map<EntityType, TagMode> display_name_visibility = new EnumMap<>(EntityType.class);
    private Integer[] default_display_name_nearby_range;
    private Integer default_display_name_threshold;
    private final Map<EntityType, Integer> display_name_threshold = new EnumMap<>(EntityType.class);


    private boolean default_death_skip_animation;
    private final Map<EntityType, Boolean> death_skip_animation = new EnumMap<>(EntityType.class);
    private final List<String> default_death_priority = new ArrayList<>();
    private final Map<EntityType, List<String>> death_priority = new EnumMap<>(EntityType.class);
    private final Map<String, Set<EntityType>> default_death_type_blacklist = new Object2ObjectOpenHashMap<>();
    private final Map<String, Set<EntityDamageEvent.DamageCause>> default_death_reason_blacklist = new Object2ObjectOpenHashMap<>();
    private final Map<EntityType, Map<String, Set<EntityDamageEvent.DamageCause>>> death_reason_blacklist = new EnumMap<>(EntityType.class);
    private int default_death_step_min_step;
    private final Map<EntityType, Integer> death_step_min_step = new EnumMap<>(EntityType.class);
    private int default_death_step_max_step;
    private final Map<EntityType, Integer> death_step_max_step = new EnumMap<>(EntityType.class);


    private boolean default_drops_enabled;
    private final Map<EntityType, Boolean> drops_enabled = new EnumMap<>(EntityType.class);
    private boolean default_drops_use_loot_tables;
    private final Map<EntityType, Boolean> drops_use_loot_tables = new EnumMap<>(EntityType.class);
    private final Set<Material> default_drops_one_per_stack = EnumSet.noneOf(Material.class);
    private final Map<EntityType, Set<Material>> drops_one_per_stack = new EnumMap<>(EntityType.class);
    private final Set<Material> default_drops_item_blacklist = EnumSet.noneOf(Material.class);
    private final Map<EntityType, Set<Material>> drops_item_blacklist = new EnumMap<>(EntityType.class);
    private final Set<EntityDamageEvent.DamageCause> default_drops_reason_blacklist = EnumSet.noneOf(EntityDamageEvent.DamageCause.class);
    private final Map<EntityType, Set<EntityDamageEvent.DamageCause>> drops_reason_blacklist = new EnumMap<>(EntityType.class);
    private final Set<EntityType> default_drops_type_blacklist = EnumSet.noneOf(EntityType.class);


    private boolean default_experience_enabled;
    private final Map<EntityType, Boolean> experience_enabled = new EnumMap<>(EntityType.class);
    private double default_experience_multiplier_min;
    private final Map<EntityType, Double> experience_multiplier_min = new EnumMap<>(EntityType.class);
    private double default_experience_multiplier_max;
    private final Map<EntityType, Double> experience_multiplier_max = new EnumMap<>(EntityType.class);
    private final Set<EntityType> default_experience_type_blacklist = new ObjectOpenHashSet<>();


    private boolean default_player_stats;
    private final Map<EntityType, Boolean> player_stats = new EnumMap<>(EntityType.class);


    private boolean default_wait_to_stack_enabled;
    private final Map<EntityType, Boolean> wait_to_stack_enabled = new EnumMap<>(EntityType.class);
    private int default_wait_to_stack_wait_time;
    private final Map<EntityType, Integer> wait_to_stack_wait_time = new EnumMap<>(EntityType.class);
    private final Set<CreatureSpawnEvent.SpawnReason> default_wait_to_stack_reasons_whitelist = EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class);
    private final Map<EntityType, Set<CreatureSpawnEvent.SpawnReason>> wait_to_stack_reasons_whitelist = new EnumMap<>(EntityType.class);
    private final Set<EntityType> default_wait_to_stack_types_whitelist = EnumSet.noneOf(EntityType.class);


    private boolean default_disable_targeting_enabled;
    private final Map<EntityType, Boolean> disable_targeting_enabled = new EnumMap<>(EntityType.class);
    private final Set<EntityType> default_disable_targeting_type_blacklist = EnumSet.noneOf(EntityType.class);
    private final Set<CreatureSpawnEvent.SpawnReason> default_disable_targeting_reason_blacklist = EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class);
    private final Map<EntityType, Set<CreatureSpawnEvent.SpawnReason>> disable_targeting_reason_blacklist = new EnumMap<>(EntityType.class);


    private final Set<EntityType> default_types_blacklist = EnumSet.noneOf(EntityType.class);
    private final Set<CreatureSpawnEvent.SpawnReason> default_reason_blacklist = EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class);
    private final Map<EntityType, Set<CreatureSpawnEvent.SpawnReason>> reason_blacklist = new EnumMap<>(EntityType.class);
    private final Set<World> default_worlds_blacklist = new ObjectOpenHashSet<>();
    private final Map<EntityType, Set<World>> worlds_blacklist = new EnumMap<>(EntityType.class);


    private final Map<String, Boolean> default_events_remove_stack_data = new Object2ObjectOpenHashMap<>();
    private final Map<EntityType, Map<String, Boolean>> events_remove_stack_data = new EnumMap<>(EntityType.class);
    private final Map<String, ListenerMode> default_events_mode = new Object2ObjectOpenHashMap<>();
    private final Map<EntityType, Map<String, ListenerMode>> events_mode = new EnumMap<>(EntityType.class);


    private boolean default_events_multiply_slime_split;

    private final StackMob sm;

    public MainConfig(StackMob sm) {
        super(sm, "config.yml");
        this.sm = sm;
    }

    public void cache() {
        default_stack_max_size = getInt("stack.max-size");
        default_stack_merge_range = getList("stack.merge-range").asIntList().toArray(new Integer[2]);
        default_stack_threshold_enabled = getBoolean("stack.threshold.enabled");
        default_stack_threshold_amount = getInt("stack.threshold.amount");


        default_display_name_format = getString("display-name.format");
        default_display_name_visibility = TagMode.valueOf(getString("display-name.visibility"));
        default_display_name_nearby_range = getList("display-name.nearby.range").asIntList().toArray(new Integer[2]);
        default_display_name_threshold = getInt("display-name.threshold");


        default_death_skip_animation = getBoolean("death.skip-animation");
        final Collection<String> death_priorities = getDeathSection(null);
        default_death_priority.addAll(death_priorities);
        for (String defaultDeathOption : death_priorities) {
            final Set<EntityType> entityTypes = default_death_type_blacklist.getOrDefault(defaultDeathOption, EnumSet.noneOf(EntityType.class));
            entityTypes.addAll(getList("death." + defaultDeathOption + ".type-blacklist").asEnumList(EntityType.class));
            default_death_type_blacklist.putIfAbsent(defaultDeathOption, entityTypes);

            final Set<EntityDamageEvent.DamageCause> damageCauses = default_death_reason_blacklist.getOrDefault(defaultDeathOption, EnumSet.noneOf(EntityDamageEvent.DamageCause.class));
            damageCauses.addAll(getList("death." + defaultDeathOption + ".reason-blacklist").asEnumList(EntityDamageEvent.DamageCause.class));
            default_death_reason_blacklist.putIfAbsent(defaultDeathOption, damageCauses);
        }
        default_death_step_max_step = getInt("death.STEP.max-step");
        default_death_step_min_step = getInt("death.STEP.min-step");


        default_drops_enabled = getBoolean("drops.enabled");
        default_drops_use_loot_tables = getBoolean("drops.use-loot-tables");
        default_drops_one_per_stack.addAll(getList("drops.one-per-stack").asEnumList(Material.class));
        default_drops_item_blacklist.addAll(getList("drops.item-blacklist").asEnumList(Material.class));
        default_drops_reason_blacklist.addAll(getList("drops.reason-blacklist").asEnumList(EntityDamageEvent.DamageCause.class));
        default_drops_type_blacklist.addAll(getList("drops.type-blacklist").asEnumList(EntityType.class));


        default_experience_enabled = getBoolean("experience.enabled");
        default_experience_multiplier_min = getDouble("experience.multiplier-min");
        default_experience_multiplier_max = getDouble("experience.multiplier-max");
        default_experience_type_blacklist.addAll(getList("experience.type-blacklist").asEnumList(EntityType.class));


        default_player_stats = getBoolean("player-stats");


        default_wait_to_stack_enabled = getBoolean("wait-to-stack.enabled");
        default_wait_to_stack_wait_time = getInt("wait-to-stack.wait-time");
        default_wait_to_stack_types_whitelist.addAll(getList("wait-to-stack.types-whitelist").asEnumList(EntityType.class));
        default_wait_to_stack_reasons_whitelist.addAll(getList("wait-to-stack.reasons-whitelist").asEnumList(CreatureSpawnEvent.SpawnReason.class));


        default_disable_targeting_enabled = getBoolean("disable-targeting.enabled");
        default_disable_targeting_type_blacklist.addAll(getList("disable-targeting.type-blacklist").asEnumList(EntityType.class));
        default_disable_targeting_reason_blacklist.addAll(getList("disable-targeting.reason-blacklist").asEnumList(CreatureSpawnEvent.SpawnReason.class));


        default_types_blacklist.addAll(getList("types-blacklist").asEnumList(EntityType.class));
        default_reason_blacklist.addAll(getList("reason-blacklist").asEnumList(CreatureSpawnEvent.SpawnReason.class));
        default_worlds_blacklist.addAll(getList("worlds-blacklist").asWorldList());


        for (String key : getConfigurationSection("events.remove-stack-data").getKeys(false)) {
            default_events_remove_stack_data.put(key, getBoolean("events.remove-stack-data." + key));
        }
        for (String key : getConfigurationSection("events.").getKeys(false)) {
            final String mode = getString("events." + key + ".mode");

            if (mode != null) {
                default_events_mode.put(key, ListenerMode.valueOf(mode));
            }
        }


        default_events_multiply_slime_split = getBoolean("events.multiply.slime-split");

        for (EntityType type : EntityType.values()) {
            final int custom_stack_max_size = getInt(type, "stack.max-size");
            if (custom_stack_max_size != default_stack_max_size) stack_max_size.put(type, custom_stack_max_size);

            final Integer[] custom_stack_merge_range = getList(type, "stack.merge-range").asIntList().toArray(new Integer[2]);
            if (!Arrays.equals(custom_stack_merge_range, default_stack_merge_range))
                stack_merge_range.put(type, default_stack_merge_range);

            final boolean custom_stack_threshold_enabled = getBoolean(type, "stack.threshold.enabled");
            if (custom_stack_threshold_enabled != default_stack_threshold_enabled)
                stack_threshold_enabled.put(type, custom_stack_threshold_enabled);

            final int custom_stack_threshold_amount = getInt(type, "stack.threshold.amount");
            if (custom_stack_threshold_amount != default_stack_threshold_amount)
                stack_threshold_amount.put(type, custom_stack_threshold_amount);


            final String custom_display_name_format = getString(type, "display-name.format");
            if (!custom_display_name_format.equals(default_display_name_format))
                display_name_format.put(type, custom_display_name_format);

            final TagMode custom_display_name_visibility = TagMode.valueOf(getString(type, "display-name.visibility"));
            if (custom_display_name_visibility != default_display_name_visibility)
                display_name_visibility.put(type, custom_display_name_visibility);

            final int custom_display_name_threshold = getInt(type, "display-name.threshold");
            if (custom_display_name_threshold != default_display_name_threshold)
                display_name_threshold.put(type, custom_display_name_threshold);


            final boolean custom_death_skip_animation = getBoolean(type, "death.skip-animation");
            if (custom_death_skip_animation != default_death_skip_animation)
                death_skip_animation.put(type, custom_death_skip_animation);

            final List<String> custom_death_priorities = new ArrayList<>(getDeathSection(type));
            if (!custom_death_priorities.equals(default_death_priority)) {
                death_priority.put(type, custom_death_priorities);

                for (String defaultDeathOption : custom_death_priorities) {
                    final Map<String, Set<EntityDamageEvent.DamageCause>> damageCauses = death_reason_blacklist.getOrDefault(type, new Object2ObjectOpenHashMap<>());
                    final Set<EntityDamageEvent.DamageCause> damageCausesSet = EnumSet.noneOf(EntityDamageEvent.DamageCause.class);
                    damageCausesSet.addAll(getList(type, "death." + defaultDeathOption + ".reason-blacklist").asEnumList(EntityDamageEvent.DamageCause.class));

                    damageCauses.put(defaultDeathOption, damageCausesSet);
                    death_reason_blacklist.putIfAbsent(type, damageCauses);
                }
            }
            final int custom_death_step_max_step = getInt(type, "death.STEP.max-step");
            if (custom_death_step_max_step != default_death_step_max_step)
                death_step_max_step.put(type, custom_death_step_max_step);

            final int custom_death_step_min_step = getInt(type, "death.STEP.min-step");
            if (custom_death_step_min_step != default_death_step_min_step)
                death_step_min_step.put(type, custom_death_step_min_step);


            final boolean custom_drops_enabled = getBoolean(type, "drops.enabled");
            if (custom_drops_enabled != default_drops_enabled) drops_enabled.put(type, custom_drops_enabled);

            final boolean custom_drops_use_loot_tables = getBoolean(type, "drops.use-loot-tables");
            if (custom_drops_use_loot_tables != default_drops_use_loot_tables)
                drops_use_loot_tables.put(type, custom_drops_use_loot_tables);

            final Set<Material> custom_drops_one_per_stack = EnumSet.noneOf(Material.class);
            custom_drops_one_per_stack.addAll(getList(type, "drops.one-per-stack").asEnumList(Material.class));
            if (!custom_drops_one_per_stack.equals(default_drops_one_per_stack))
                drops_one_per_stack.put(type, custom_drops_one_per_stack);

            final Set<Material> custom_drops_item_blacklist = EnumSet.noneOf(Material.class);
            custom_drops_item_blacklist.addAll(getList(type, "drops.item-blacklist").asEnumList(Material.class));
            if (!custom_drops_item_blacklist.equals(default_drops_item_blacklist))
                drops_item_blacklist.put(type, custom_drops_item_blacklist);

            final Set<EntityDamageEvent.DamageCause> custom_drops_reason_blacklist = EnumSet.noneOf(EntityDamageEvent.DamageCause.class);
            custom_drops_reason_blacklist.addAll(getList(type, "drops.reason-blacklist").asEnumList(EntityDamageEvent.DamageCause.class));
            if (!custom_drops_reason_blacklist.equals(default_drops_reason_blacklist))
                drops_reason_blacklist.put(type, custom_drops_reason_blacklist);


            final boolean custom_experience_enabled = getBoolean(type, "experience.enabled");
            if (custom_experience_enabled != default_experience_enabled)
                experience_enabled.put(type, custom_experience_enabled);

            final double custom_experience_multiplier_min = getDouble(type, "experience.multiplier-min");
            if (custom_experience_multiplier_min != default_experience_multiplier_min)
                experience_multiplier_min.put(type, custom_experience_multiplier_min);

            final double custom_experience_multiplier_max = getDouble(type, "experience.multiplier-max");
            if (custom_experience_multiplier_max != default_experience_multiplier_max)
                experience_multiplier_max.put(type, custom_experience_multiplier_max);


            final boolean custom_player_stats = getBoolean(type, "player-stats");
            if (custom_player_stats != default_player_stats) player_stats.put(type, custom_player_stats);


            final boolean custom_wait_to_stack_enabled = getBoolean(type, "wait-to-stack.enabled");
            if (custom_wait_to_stack_enabled != default_wait_to_stack_enabled)
                wait_to_stack_enabled.put(type, custom_wait_to_stack_enabled);

            final int custom_wait_to_stack_wait_time = getInt(type, "wait-to-stack.wait-time");
            if (custom_wait_to_stack_wait_time != default_wait_to_stack_wait_time)
                wait_to_stack_wait_time.put(type, custom_wait_to_stack_wait_time);

            final Set<CreatureSpawnEvent.SpawnReason> custom_wait_to_stack_reasons_whitelist = EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class);
            custom_wait_to_stack_reasons_whitelist.addAll(getList(type, "wait-to-stack.reasons-whitelist").asEnumList(CreatureSpawnEvent.SpawnReason.class));
            if (!custom_wait_to_stack_reasons_whitelist.equals(default_wait_to_stack_reasons_whitelist)) {
                wait_to_stack_reasons_whitelist.put(type, custom_wait_to_stack_reasons_whitelist);
            }


            final boolean custom_disable_targeting_enabled = getBoolean(type, "disable-targeting.enabled");
            if (custom_disable_targeting_enabled != default_disable_targeting_enabled)
                disable_targeting_enabled.put(type, custom_disable_targeting_enabled);

            final Set<CreatureSpawnEvent.SpawnReason> custom_disable_targeting_reason_blacklist = EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class);
            custom_disable_targeting_reason_blacklist.addAll(getList(type, "disable-targeting.reason-blacklist").asEnumList(CreatureSpawnEvent.SpawnReason.class));
            if (!custom_disable_targeting_reason_blacklist.equals(default_disable_targeting_reason_blacklist))
                disable_targeting_reason_blacklist.put(type, custom_disable_targeting_reason_blacklist);


            final Set<CreatureSpawnEvent.SpawnReason> custom_reason_blacklist = EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class);
            custom_reason_blacklist.addAll(getList(type, "reason-blacklist").asEnumList(CreatureSpawnEvent.SpawnReason.class));
            if (!custom_reason_blacklist.equals(default_reason_blacklist))
                reason_blacklist.put(type, custom_reason_blacklist);

            final Set<World> custom_worlds_blacklist = new ObjectOpenHashSet<>(getList(type, "worlds-blacklist").asWorldList());
            if (!custom_worlds_blacklist.equals(default_worlds_blacklist))
                worlds_blacklist.put(type, custom_worlds_blacklist);


            for (String key : getConfigurationSection(type, "events.remove-stack-data").getKeys(false)) {
                final Map<String, Boolean> map = events_remove_stack_data.getOrDefault(type, new Object2BooleanOpenHashMap<>());
                final boolean custom_events_remove_stack_data = getBoolean(type, "events.remove-stack-data." + key);
                if (custom_events_remove_stack_data != default_events_remove_stack_data.get(key)) {
                    map.put(key, custom_events_remove_stack_data);
                    events_remove_stack_data.putIfAbsent(type, map);
                }
            }
            for (String key : getConfigurationSection(type, "events.").getKeys(false)) {
                final Map<String, ListenerMode> map = events_mode.getOrDefault(type, new Object2ObjectOpenHashMap<>());
                final String custom_mode = getString(type, "events." + key + ".mode");

                if (custom_mode != null && !custom_mode.equals(default_events_mode.get(key).toString())) {
                    map.put(key, ListenerMode.valueOf(custom_mode));
                    events_mode.putIfAbsent(type, map);
                }
            }
        }
    }

    public int getMaxStack(EntityType type) {
        return stack_max_size.getOrDefault(type, default_stack_max_size);
    }

    public boolean getStackThresholdEnabled(EntityType type) {
        return stack_threshold_enabled.getOrDefault(type, default_stack_threshold_enabled);
    }

    public int getStackThreshold(EntityType type) {
        return stack_threshold_amount.getOrDefault(type, default_stack_threshold_amount);
    }

    public Integer[] getStackRadius(EntityType type) {
        return stack_merge_range.getOrDefault(type, default_stack_merge_range);
    }

    public int getStackInterval() {
        return getInt("stack.interval");
    }

    public String getTagFormat(EntityType type) {
        return display_name_format.getOrDefault(type, default_display_name_format);
    }

    public int getTagThreshold(EntityType type) {
        return display_name_threshold.getOrDefault(type, default_display_name_threshold);
    }

    public TagMode getTagMode(EntityType type) {
        return display_name_visibility.getOrDefault(type, default_display_name_visibility);
    }

    public Integer[] getTagNearbyRadius() {
        return default_display_name_nearby_range;
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
        return drops_enabled.getOrDefault(type, default_drops_enabled);
    }

    public boolean isDropLootTables(EntityType type) {
        return drops_use_loot_tables.getOrDefault(type, default_drops_use_loot_tables);
    }

    public boolean isSlimeMultiEnabled() {
        return default_events_multiply_slime_split;
    }

    public boolean isDropTypeBlacklisted(EntityType type) {
        return default_drops_type_blacklist.contains(type);
    }

    public boolean isDropReasonBlacklisted(EntityType type, EntityDamageEvent.DamageCause cause) {
        return drops_reason_blacklist.getOrDefault(type, default_drops_reason_blacklist).contains(cause);
    }

    public boolean idDropItemBlacklisted(EntityType type, Material material) {
        return drops_item_blacklist.getOrDefault(type, default_drops_item_blacklist).contains(material);
    }

    public boolean isDropItemOnePer(EntityType type, Material material) {
        return drops_one_per_stack.getOrDefault(type, default_drops_one_per_stack).contains(material);
    }

    public boolean isExpMultiEnabled(EntityType type) {
        return experience_enabled.getOrDefault(type, default_experience_enabled);
    }

    public boolean isExpTypeBlacklisted(EntityType type) {
        return default_experience_type_blacklist.contains(type);
    }

    public double getExpMinBound(EntityType type) {
        return experience_multiplier_min.getOrDefault(type, default_experience_multiplier_min);
    }

    public double getExpMaxBound(EntityType type) {
        return experience_multiplier_max.getOrDefault(type, default_experience_multiplier_max);
    }

    public boolean isPlayerStatMulti(EntityType type) {
        return player_stats.getOrDefault(type, default_player_stats);
    }

    public boolean isWaitingEnabled(EntityType type) {
        return wait_to_stack_enabled.getOrDefault(type, default_wait_to_stack_enabled);
    }

    public boolean isWaitingType(EntityType type) {
        return default_wait_to_stack_types_whitelist.contains(type);
    }

    public boolean isWaitingReason(EntityType type, CreatureSpawnEvent.SpawnReason reason) {
        return wait_to_stack_reasons_whitelist.getOrDefault(type, default_wait_to_stack_reasons_whitelist).contains(reason);
    }

    public int getWaitingTime(EntityType type) {
        return wait_to_stack_wait_time.getOrDefault(type, default_wait_to_stack_wait_time);
    }

    public int getMaxDeathStep(EntityType type) {
        return death_step_max_step.getOrDefault(type, default_death_step_max_step);
    }

    public int getMinDeathStep(EntityType type) {
        return death_step_min_step.getOrDefault(type, default_death_step_min_step);
    }

    public boolean removeStackDataOnDivide(EntityType type, String reasonKey) {
        return events_remove_stack_data.getOrDefault(type, default_events_remove_stack_data).get(reasonKey);
    }

    public boolean isTargetingDisabled(EntityType type) {
        return disable_targeting_enabled.getOrDefault(type, default_disable_targeting_enabled);
    }

    public boolean isTargetingDisabledType(EntityType type) {
        return default_disable_targeting_type_blacklist.contains(type);
    }

    public boolean isTargetingDisabledReason(EntityType type, CreatureSpawnEvent.SpawnReason reason) {
        return disable_targeting_reason_blacklist.getOrDefault(type, default_disable_targeting_reason_blacklist).contains(reason);
    }

    public ListenerMode getListenerMode(EntityType type, String eventKey) {
        return events_mode.getOrDefault(type, default_events_mode).get(eventKey);
    }

    public boolean isEntityBlacklisted(LivingEntity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (default_types_blacklist.contains(entity.getType())) {
            return true;
        }
        if (reason_blacklist.getOrDefault(entity.getType(), default_reason_blacklist).contains(reason)) {
            return true;
        }
        return worlds_blacklist.getOrDefault(entity.getType(), default_worlds_blacklist).contains(entity.getWorld());
    }

    public DeathType getDeathType(LivingEntity entity) {
        for (String key : death_priority.getOrDefault(entity.getType(), default_death_priority)) {
            if (death_reason_blacklist.getOrDefault(entity.getType(), default_death_reason_blacklist).get(key).contains(entity.getLastDamageCause().getCause())) {
                continue;
            }
            if (default_death_type_blacklist.get(key).contains(entity.getType())) {
                continue;
            }
            return DeathType.valueOf(key);
        }
        throw new UnsupportedOperationException("Configuration error - unable to determine death type!");
    }

    private Collection<String> getDeathSection(EntityType type) {
        final Map<Integer, String> array = new TreeMap<>();

        if (type == null) {
            for (DeathType deathType : DeathType.values()) {
                array.put(getInt("death." + deathType.name() + ".priority"), deathType.name());
            }
        } else {
            for (String key : getConfigurationSection(type, "death").getKeys(false)) {
                if (!key.toUpperCase().equals(key)) continue;
                array.put(getInt(type, "death." + key + ".priority"), key);
            }
        }

        return array.values();
    }

    public boolean isSkipDeathAnimation(EntityType type) {
        return death_skip_animation.getOrDefault(type, default_death_skip_animation);
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

}