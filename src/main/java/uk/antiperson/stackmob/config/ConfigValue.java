package uk.antiperson.stackmob.config;

public class ConfigValue {

    private final String path;
    private final Object value;
    public ConfigValue(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }
}
