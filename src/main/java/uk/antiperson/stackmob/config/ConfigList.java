package uk.antiperson.stackmob.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.ArrayList;
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

    public List<EntityType> asEntityTypeList() {
        final List<EntityType> entityTypeList = new ArrayList<>();

        for (EntityType entityType : EntityType.values()) {
            if (contains(entityType.toString())) {
                entityTypeList.add(entityType);
            }
        }

        return entityTypeList;
    }

    public List<Material> asMaterialList() {
        final List<Material> materialList = new ArrayList<>();

        for (Material material : Material.values()) {
            if (contains(material.toString())) {
                materialList.add(material);
            }
        }

        return materialList;
    }

    public List<CreatureSpawnEvent.SpawnReason> asSpawnReasonList() {
        final List<CreatureSpawnEvent.SpawnReason> spawnReasonList = new ArrayList<>();

        for (CreatureSpawnEvent.SpawnReason spawnReason : CreatureSpawnEvent.SpawnReason.values()) {
            if (contains(spawnReason.toString())) {
                spawnReasonList.add(spawnReason);
            }
        }

        return spawnReasonList;
    }

    public List<EntityTargetEvent.TargetReason> asTargetReasonList() {
        final List<EntityTargetEvent.TargetReason> targetReasonList = new ArrayList<>();

        for (EntityTargetEvent.TargetReason targetReason : EntityTargetEvent.TargetReason.values()) {
            if (contains(targetReason.toString())) {
                targetReasonList.add(targetReason);
            }
        }

        return targetReasonList;
    }

    public List<EntityDamageEvent.DamageCause> asDamageCauseList() {
        final List<EntityDamageEvent.DamageCause> damageCauseList = new ArrayList<>();

        for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
            if (contains(damageCause.toString())) {
                damageCauseList.add(damageCause);
            }
        }

        return damageCauseList;
    }

    public List<World> asWorldList() {
        final List<World> worldList = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            if (contains(world.getName())) {
                worldList.add(world);
            }
        }

        return worldList;
    }

}
