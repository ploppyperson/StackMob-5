package uk.antiperson.stackmob.config;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.EntityType;
import uk.antiperson.stackmob.StackMob;

import java.util.List;

public abstract class SpecialConfigFile extends ConfigFile {

    public SpecialConfigFile(StackMob sm, String filePath) {
        super(sm, filePath);
    }

    public ConfigList getList(EntityType type, String path) {
        ConfigValue configValue = get(type, path);
        return configValue.getValue() instanceof List<?> ? ConfigList.getConfigList(this, new ConfigValue(configValue.getPath(), configValue.getValue())) : null;
    }

    public boolean getBoolean(EntityType type, String path) {
        Object value = getValue(type, path);
        return value instanceof Boolean ? (Boolean) value : false;
    }

    public double getDouble(EntityType type, String path) {
        Object value = getValue(type, path);
        return value instanceof Double ? NumberUtils.toDouble(value.toString()) : 0;
    }

    public int getInt(EntityType type, String path) {
        Object value = getValue(type, path);
        return value instanceof Number ? NumberUtils.toInt(value.toString()) : 0;
    }

    public String getString(EntityType type, String path) {
        Object value = getValue(type, path);
        return value == null ? null : value.toString();
    }

    private Object getValue(EntityType type, String path) {
        return get(type, path).getValue();
    }

    private ConfigValue get(EntityType type, String path) {
        if (!isFileLoaded()) {
            throw new UnsupportedOperationException("Configuration file has not been loaded!");
        }
        // Check if the specified general config path is overridden by an entity specific equivalent.
        String customPath = "custom." + type + "." + path;
        Object customValue = get(customPath);
        if (customValue != null) {
            return new ConfigValue(customPath, customValue);
        }
        // Check if this entity specific path is specified to clone another path.
        String clone = "custom." + type + ".clone";
        String clonePath = "custom." + getString(clone) + "." + path;
        Object cloneValue = get(clonePath);
        if (cloneValue != null) {
            return new ConfigValue(clonePath, cloneValue);
        }
        return new ConfigValue(path, get(path));
    }

    /**
     * Gets the custom config path for this entity if it has been specified. If not this will just be the normal value.
     * @param type the entity type
     * @param path the config path.
     * @return the path for this config path and entity.
     */
    private String getPath(EntityType type, String path) {
        if (!isFileLoaded()) {
            throw new UnsupportedOperationException("Configuration file has not been loaded!");
        }
        // Check if the specified general config path is overridden by an entity specific equivalent.
        String customPath = "custom." + type + "." + path;
        if (isSet(customPath)) {
            return customPath;
        }
        // Check if this entity specific path is specified to clone another path.
        String clone = "custom." + type + ".clone";
        String clonePath = "custom." + getString(clone) + "." + path;
        if (isString(clonePath)) {
            return clonePath;
        }
        return path;
    }
}
