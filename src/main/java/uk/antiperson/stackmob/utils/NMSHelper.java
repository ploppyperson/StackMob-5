package uk.antiperson.stackmob.utils;

import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class NMSHelper {

    public static void sendPacket(Player player, Entity entity, boolean tagVisible) throws NoSuchFieldException, IllegalAccessException {
        CraftEntity craftEntity = (CraftEntity) entity;
        Field field = net.minecraft.server.v1_15_R1.Entity.class.getDeclaredField("aA");
        field.setAccessible(true);
        DataWatcherObject<Boolean> datawatcherobject = (DataWatcherObject<Boolean>) field.get(null);
        DataWatcher watcher = craftEntity.getHandle().getDataWatcher();
        watcher.set(datawatcherobject, tagVisible);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(craftEntity.getHandle().getId(), watcher, false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
