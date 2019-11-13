package uk.antiperson.stackmob.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigList {

    private String path;
    private FileConfiguration fileCon;
    public ConfigList(FileConfiguration fileCon, String path) {
        this.fileCon = fileCon;
        this.path = path;
    }

    /**
     * List contains method which supports inverting lists.
     * @param tocheck object to check is in the list
     * @return whether this object is in the list.
     */
    public boolean contains(Object tocheck) {
        List<?> list = fileCon.getList(path);
        if (list == null) {
            throw new UnsupportedOperationException(path + " list is null!");
        }
        if (fileCon.getBoolean(path + "-invert")){
            return !list.contains(tocheck);
        }
        return list.contains(tocheck);
    }

    public List<Integer> asIntList() {
        return fileCon.getIntegerList(path);
    }
}
