package uk.antiperson.stackmob.packets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
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

import java.util.Optional;

public class NmsFakeArmorStand implements FakeArmorStand {

    private EntityArmorStand entityArmorStand;
    private final Player player;

    public NmsFakeArmorStand(Player player) {
        this.player = player;
    }

    public void spawnFakeArmorStand(Entity owner, Location location, Component name, double offset) {
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        // spawn armor stand
        Location adjusted = adjustLocation(owner, offset);
        entityArmorStand = new EntityArmorStand(worldServer, adjusted.getX(), adjusted.getY(), adjusted.getZ());
        // metadata for armour stand
        // send spawn packet
        PacketPlayOutSpawnEntity packetPlayOutSpawn = new PacketPlayOutSpawnEntity(entityArmorStand);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutSpawn);
        DataWatcher watcher = new DataWatcher(entityArmorStand);
        watcher.a(new DataWatcherObject<>(0, DataWatcherRegistry.a), (byte) 0x20);
        watcher.a(new DataWatcherObject<>(2, DataWatcherRegistry.f), Optional.ofNullable(IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serializeToTree(name))));
        watcher.a(new DataWatcherObject<>(3, DataWatcherRegistry.i), true);
        watcher.a(new DataWatcherObject<>(5, DataWatcherRegistry.i), true);
        watcher.a(new DataWatcherObject<>(15, DataWatcherRegistry.a), (byte) 0x10);
        // send metadata packet for armor stand
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entityArmorStand.ae(), watcher, true);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutEntityMetadata);
    }

    @Override
    public void updateName(Component newName) {
        DataWatcher watcher = new DataWatcher(entityArmorStand);
        watcher.a(new DataWatcherObject<>(2, DataWatcherRegistry.f), Optional.ofNullable(IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serializeToTree(newName))));
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entityArmorStand.ae(), watcher, true);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutEntityMetadata);
    }

    public void teleport(Entity entity, double offset) {
        Location adjusted = adjustLocation(entity, offset);
        entityArmorStand.g(adjusted.getX(), adjusted.getY(), adjusted.getZ());
        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(entityArmorStand);
        ((CraftPlayer) player).getHandle().b.a(teleport);
    }

    public void removeFakeArmorStand() {
        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.ae());
        ((CraftPlayer) player).getHandle().b.a(entityDestroy);
    }

}
