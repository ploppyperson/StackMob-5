package uk.antiperson.stackmob.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

    public String getString(String path) {
        return fileCon.getString(path);
    }

    public int getInt(String path) {
        return fileCon.getInt(path);
    }

    public double getDouble(String path) {
        return fileCon.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return fileCon.getBoolean(path);
    }

    public ConfigList getList(String path) {
        return new ConfigList(fileCon, path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return fileCon.getConfigurationSection(path);
    }

    public boolean isSet(String path) {
        return fileCon.isSet(path);
    }

    public boolean isString(String path) {
        return fileCon.isString(path);
    }

    public boolean isFileLoaded() {
        return fileCon != null;
    }

    public void set(String path, Object value) {
        fileCon.set(path, value);
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

    /**
     * Saves the config to file.
     * NOTE: Comments will be removed!
     * @throws IOException when I/O error occurs
     */
    public void save() throws IOException {
        fileCon.save(file);
    }

}
