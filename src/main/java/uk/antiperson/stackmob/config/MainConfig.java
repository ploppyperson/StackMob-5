package uk.antiperson.stackmob.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainConfig {

    private final Map<EntityType, EntityConfig> map;
    private final MainConfigFile configFile;
    private final StackMob sm;

    public MainConfig(StackMob sm) {
        this.sm = sm;
        this.map = new HashMap<>();
        this.configFile = new MainConfigFile(sm);
    }

    public void init() throws IOException {
        configFile.load();
        for (EntityType entityType : EntityType.values()) {
            EntityConfig entityConfig = new EntityConfig(entityType);
            for (String key : configFile.getKeys(true)) {
                if (key.startsWith("custom")) {
                    continue;
                }
                entityConfig.put(key, new ConfigValue(configFile, key, configFile.get(key)));
            }
            map.put(entityType, entityConfig);
        }
        ConfigurationSection custom = configFile.getConfigurationSection("custom");
        if (custom == null) {
            return;
        }
        for (String key : custom.getKeys(false)) {
            EntityType toRead = EntityType.valueOf(key);
            EntityType cloneType = null;
            String clone = custom.getString(toRead + ".clone", null);
            if (clone != null && clone.length() > 0) {
                cloneType = EntityType.valueOf(clone);
            }
            EntityType[] array = new EntityType[]{cloneType, toRead};
            EntityConfig entityConfig = map.get(toRead);
            for (EntityType type : array) {
                if (type == null) {
                    continue;
                }
                ConfigurationSection section = configFile.getConfigurationSection("custom." + type);
                for (String customKey : section.getKeys(true)) {
                    if (customKey.equals("clone")) {
                        continue;
                    }
                    String actualKey = "custom." + type + "." + customKey;
                    entityConfig.put(customKey, new ConfigValue(configFile, actualKey, section.get(customKey)));
                }
            }
        }
    }

    public void reload() throws IOException {
        init();
        sm.getEntityManager().getStackEntities().forEach(StackEntity::refreshConfig);
    }

    public EntityConfig getConfig(EntityType type) {
        return map.get(type);
    }

    public EntityConfig getConfig(Entity entity) {
        return getConfig(entity.getType());
    }

    public EntityConfig getConfig() {
        return map.get(EntityType.UNKNOWN);
    }

    public MainConfigFile getConfigFile() {
        return configFile;
    }
}
