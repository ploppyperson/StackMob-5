package uk.antiperson.stackmob.packets;

import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NMSHelper {

    public static void sendVisibilityPacket(Player player, Entity entity, boolean tagVisible) {
        DataWatcher.b<Boolean> af = DataWatcher.b.a(new DataWatcherObject<>(3, DataWatcherRegistry.k), tagVisible);
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getEntityId(), Collections.singletonList(af));
        ((CraftPlayer) player).getHandle().c.a(packetPlayOutEntityMetadata);
    }
}
