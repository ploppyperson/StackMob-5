package uk.antiperson.stackmob.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigFile {

    private File file;
    private FileConfiguration fileCon;
    private StackMob sm;
    private String filePath;
    public ConfigFile(StackMob sm, String filePath) {
        this.sm = sm;
        this.filePath = filePath;
    }

    public String getString(EntityType type, String path) {
        return fileCon.getString(getPath(type, path));
    }

    public String getString(String path) {
        return fileCon.getString(path);
    }

    public int getInt(EntityType type, String path) {
        return fileCon.getInt(getPath(type, path));
    }

    public int getInt(String path) {
        return fileCon.getInt(path);
    }

    public boolean getBoolean(EntityType type, String path) {
        return fileCon.getBoolean(getPath(type, path));
    }

    public boolean getBoolean(String path) {
        return fileCon.getBoolean(path);
    }

    public ConfigList getList(EntityType type, String path) {
        return new ConfigList(fileCon, getPath(type, path));
    }

    public ConfigList getList(String path) {
        return new ConfigList(fileCon, path);
    }

    public ConfigurationSection getConfigurationSection(EntityType type, String path) {
        return fileCon.getConfigurationSection(getPath(type, path));
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return fileCon.getConfigurationSection(path);
    }

    public boolean isSet(String path) {
        return fileCon.isSet(path);
    }

    /**
     * Gets the custom config path for this entity if it has been specified. If not this will just be the normal value.
     * @param type the entity type
     * @param path the config path.
     * @return the path for this config path and entity.
     */
    private String getPath(EntityType type, String path) {
        if (fileCon == null) {
            throw new UnsupportedOperationException("Configuration file has not been loaded!");
        }
        // Check if the specified general config path is overridden by an entity specific equivalent.
        String customPath = "custom." + type + "." + path;
        if (fileCon.isSet(customPath)) {
            return customPath;
        }
        // Check if this entity specific path is specified to clone another path.
        String clone = "custom." + type + ".clone";
        String clonePath = "custom." + fileCon.getString(clone) + "." + path;
        if (fileCon.isString(clonePath)) {
            return clonePath;
        }
        return path;
    }

    /**
     * Loads the config so that it can be read.
     * @throws IOException when an I/O error occurs if a new file is made.
     */
    public void load() throws IOException {
        file = new File(sm.getDataFolder(), filePath);
        if (!file.exists()) {
            createFile();
        }
        fileCon = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Rename the current file and create a new one from defaults.
     * @throws IOException when an I/O error occurs reading or writing.
     */
    public void makeOld() throws IOException {
        File oldFile = new File(sm.getDataFolder(), filePath.replace(".yml", ".old"));
        Files.move(file.toPath(), oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        createFile();
    }

    /**
     * Copies the default config from the jar to the plugin folder.
     * @throws IOException when an I/O error occurs reading or writing.
     */
    public void createFile() throws IOException {
        // The files copy below will throw an error if the directories are not pre existing.
        file = new File(sm.getDataFolder(), filePath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            Files.createDirectories(parentFile.toPath());
        }
        // Open the file and copy it to the plugin folder.
        InputStream is = sm.getResource(file.getName());
        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        load();
    }

}
