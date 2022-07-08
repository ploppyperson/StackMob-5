package uk.antiperson.stackmob.packets;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NmsFakeArmorStand implements FakeArmorStand {

    private EntityArmorStand entityArmorStand;

    public void spawnFakeArmorStand(Player player, Entity owner, Location location, String name) {
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        // spawn armor stand
        Location adjusted = adjustLocation(owner);
        entityArmorStand = new EntityArmorStand(worldServer, adjusted.getX(), adjusted.getY(), adjusted.getZ());
        // metadata for armour stand
        // send spawn packet
        PacketPlayOutSpawnEntity packetPlayOutSpawn = new PacketPlayOutSpawnEntity(entityArmorStand);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutSpawn);
        DataWatcher watcher = new DataWatcher(entityArmorStand);
        watcher.a(new DataWatcherObject<>(0, DataWatcherRegistry.a), (byte) 0x20);
        watcher.a(new DataWatcherObject<>(2, DataWatcherRegistry.f), java.util.Optional.ofNullable(IChatBaseComponent.a(name)));
        watcher.a(new DataWatcherObject<>(3, DataWatcherRegistry.i), true);
        watcher.a(new DataWatcherObject<>(5, DataWatcherRegistry.i), true);
        watcher.a(new DataWatcherObject<>(15, DataWatcherRegistry.a), (byte) 0x10);
        // send metadata packet for armor stand
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entityArmorStand.ae(), watcher, true);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutEntityMetadata);
    }

    public void teleport(Player player, Entity entity) {
        Location adjusted = adjustLocation(entity);
        entityArmorStand.g(adjusted.getX(), adjusted.getY(), adjusted.getZ());
        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(entityArmorStand);
        ((CraftPlayer) player).getHandle().b.a(teleport);
    }

    public void removeFakeArmorStand(Player player) {
        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.ae());
        ((CraftPlayer) player).getHandle().b.a(entityDestroy);
    }

}
