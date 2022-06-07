package uk.antiperson.stackmob.utils;

import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSHelper {

    public static void sendPacket(Player player, Entity entity, boolean tagVisible) {
        CraftEntity craftEntity = (CraftEntity) entity;
        DataWatcher watcher = new DataWatcher(craftEntity.getHandle());
        watcher.a(new DataWatcherObject<>(3, DataWatcherRegistry.i), tagVisible);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(craftEntity.getEntityId(), watcher, true);
        ((CraftPlayer) player).getHandle().b.a(packet);
    }
}
