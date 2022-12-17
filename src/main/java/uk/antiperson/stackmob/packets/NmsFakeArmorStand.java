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
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NmsFakeArmorStand implements FakeArmorStand {

    private EntityArmorStand entityArmorStand;
    private int id;
    private final Player player;

    public NmsFakeArmorStand(Player player) {
        this.player = player;
    }

    public void spawnFakeArmorStand(Entity owner, Location location, Component name, double offset) {
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        // spawn armor stand
        Location adjusted = adjustLocation(owner, offset);
        entityArmorStand = new EntityArmorStand(worldServer, adjusted.getX(), adjusted.getY(), adjusted.getZ());
        id = entityArmorStand.ah();
        // metadata for armour stand
        // send spawn packet
        PacketPlayOutSpawnEntity packetPlayOutSpawn = new PacketPlayOutSpawnEntity(entityArmorStand);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutSpawn);
        DataWatcher.b<Byte> ab = DataWatcher.b.a(new DataWatcherObject<>(0, DataWatcherRegistry.a), (byte) 0x20);
        DataWatcher.b<Optional<IChatBaseComponent>> ac = DataWatcher.b.a(new DataWatcherObject<>(2, DataWatcherRegistry.g), Optional.of(IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serializeToTree(name))));
        DataWatcher.b<Boolean> ad = DataWatcher.b.a(new DataWatcherObject<>(3, DataWatcherRegistry.j), true);
        DataWatcher.b<Boolean> ae = DataWatcher.b.a(new DataWatcherObject<>(5, DataWatcherRegistry.j), true);
        DataWatcher.b<Byte> af = DataWatcher.b.a(new DataWatcherObject<>(15, DataWatcherRegistry.a), (byte) 0x10);
        // send metadata packet for armor stand
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(id, List.of(ab, ac, ad, ae, af));
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutEntityMetadata);
    }

    @Override
    public void updateName(Component newName) {
        DataWatcher.b<Optional<IChatBaseComponent>> ad = DataWatcher.b.a(new DataWatcherObject<>(2, DataWatcherRegistry.g), Optional.of(IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serializeToTree(newName))));
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(id, Collections.singletonList(ad));
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutEntityMetadata);
    }

    public void teleport(Entity entity, double offset) {
        Location adjusted = adjustLocation(entity, offset);
        entityArmorStand.b(adjusted.getX(), adjusted.getY(), adjusted.getZ());
        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(entityArmorStand);
        ((CraftPlayer) player).getHandle().b.a(teleport);
    }

    public void removeFakeArmorStand() {
        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(id);
        ((CraftPlayer) player).getHandle().b.a(entityDestroy);
    }

}
