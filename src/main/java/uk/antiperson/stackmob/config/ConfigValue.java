package uk.antiperson.stackmob.config;

import org.apache.commons.lang.math.NumberUtils;

import java.util.Collections;
import java.util.List;

public class ConfigValue {

    private final String path;
    private final Object value;
    private final ConfigFile configFile;
    public ConfigValue(ConfigFile configFile, String path, Object value) {
        this.configFile = configFile;
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    public ConfigList getList() {
        Object obj = getValue() instanceof List<?> ? getValue() : Collections.emptyList();
        boolean inverted = configFile.getBoolean(getPath() + "-invert");
        return new ConfigList(configFile, (List<?>) obj, getPath(), inverted);
    }

    public boolean getBoolean() {
        return getValue() instanceof Boolean ? (Boolean) getValue() : false;
    }

    public double getDouble() {
        if (getValue() instanceof Integer) {
            return NumberUtils.toInt(getValue().toString());
        }
        return getValue() instanceof Double ? NumberUtils.toDouble(getValue().toString()) : 0;
    }

    public int getInt() {
        return getValue() instanceof Number ? NumberUtils.toInt(getValue().toString()) : 0;
    }

    public String getString() {
        return getValue() == null ? null : getValue().toString();
    }

    public List<Integer> asIntList() {
        return getList().asIntList();
    }
}
