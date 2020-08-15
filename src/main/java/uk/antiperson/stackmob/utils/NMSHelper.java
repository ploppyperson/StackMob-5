package uk.antiperson.stackmob.utils;

import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class NMSHelper {

    public static void sendPacket(Player player, org.bukkit.entity.Entity entity, boolean tagVisible) throws NoSuchFieldException, IllegalAccessException {
        CraftEntity craftEntity = (CraftEntity) entity;
        Field field = Entity.class.getDeclaredField("ar");
        field.setAccessible(true);
        DataWatcherObject<Boolean> datawatcherobject = (DataWatcherObject<Boolean>) field.get(null);
        DataWatcher watcher = craftEntity.getHandle().getDataWatcher();
        watcher.set(datawatcherobject, tagVisible);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(craftEntity.getHandle().getId(), watcher, false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
