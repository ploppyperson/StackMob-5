package uk.antiperson.stackmob.hook.hooks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.Hook;
import uk.antiperson.stackmob.hook.HookMetadata;
import uk.antiperson.stackmob.utils.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@HookMetadata(name = "ProtocolLib", config = "protocollib")
public class ProtocolLibHook extends Hook {

    private ProtocolManager protocolManager;
    private int entityIdCounter;
    public ProtocolLibHook(StackMob sm) {
        super(sm);
        this.entityIdCounter = Integer.MAX_VALUE / 2;
    }

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void sendPacket(Player player, Entity entity, boolean visible) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher(entity);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), visible);
        packetContainer.getEntityModifier(entity.getWorld()).write(0, entity);
        packetContainer.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            protocolManager.sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public int spawnFakeArmorStand(Player player, Location location, Component name) {
        // spawn packet
        int entityId = entityIdCounter;
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        packetContainer.getIntegers().write(0, entityId);
        packetContainer.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        packetContainer.getUUIDs().write(0, UUID.randomUUID());
        packetContainer.getDoubles().write(0, location.getX());
        packetContainer.getDoubles().write(1, location.getY());
        packetContainer.getDoubles().write(2, location.getZ());
        // metadata packet
        PacketContainer packetContainer1 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        // #getHandle() req on older than 1.19
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), Optional.of(WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(name)).getHandle()));
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        if (Utilities.getMinecraftVersion() == Utilities.MinecraftVersion.V1_16_R1) {
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x10);
        } else {
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x10);
        }
        packetContainer1.getIntegers().write(0, entityIdCounter);
        packetContainer1.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        entityIdCounter += 1;
        try {
            protocolManager.sendServerPacket(player, packetContainer);
            protocolManager.sendServerPacket(player, packetContainer1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return entityId;
    }

    public void updateTag(Player player, int id, Component newName) {
        PacketContainer packetContainer1 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), Optional.of(WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(newName)).getHandle()));
        packetContainer1.getIntegers().write(0, id);
        packetContainer1.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            protocolManager.sendServerPacket(player, packetContainer1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void teleport(Player player, int id, Location location) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        packetContainer.getIntegers().write(0, id);
        packetContainer.getDoubles().write(0, location.getX());
        packetContainer.getDoubles().write(1, location.getY());
        packetContainer.getDoubles().write(2, location.getZ());
        try {
            protocolManager.sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void removeFakeArmorStand(Player player, int id) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        if (Utilities.isVersionAtLeast(Utilities.MinecraftVersion.V1_17_R1)) {
            packetContainer.getIntLists().write(0, List.of(id));
        } else {
            packetContainer.getIntegerArrays().write(0, new int[]{id});
        }
        try {
            protocolManager.sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
