package uk.antiperson.stackmob.packets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.hooks.ProtocolLibHook;

public class ProtocolLibFakeArmorStand implements FakeArmorStand {

    private final StackMob sm;
    private ProtocolLibHook plh;
    private int entityId;
    public ProtocolLibFakeArmorStand(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public void spawnFakeArmorStand(Player player, Entity owner, Location location, String name) {
        if (plh == null) {
            plh = sm.getHookManager().getProtocolLibHook();
        }
        entityId = plh.spawnFakeArmorStand(player, adjustLocation(owner), name);
    }

    @Override
    public void teleport(Player player, Entity entity) {
        if (plh == null) {
            plh = sm.getHookManager().getProtocolLibHook();
        }
        plh.teleport(player, entityId, adjustLocation(entity));
    }

    @Override
    public void removeFakeArmorStand(Player player) {
        if (plh == null) {
            plh = sm.getHookManager().getProtocolLibHook();
        }
        plh.removeFakeArmorStand(player, entityId);
    }
}
