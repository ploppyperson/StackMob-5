package uk.antiperson.stackmob.packets;

import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.hook.hooks.ProtocolLibHook;
import uk.antiperson.stackmob.utils.Utilities;

public class TagHandler {

    private boolean tagVisible;
    private final StackEntity stackEntity;
    private final StackMob sm;
    private final Player player;
    private FakeArmorStand fakeArmorStand;

    public TagHandler(StackMob sm, Player player, StackEntity stackEntity) {
        this.sm = sm;
        this.stackEntity = stackEntity;
        this.player = player;
    }

    public void init() {
        if (Utilities.getMinecraftVersion() != Utilities.NMS_VERSION) {
            this.fakeArmorStand = new ProtocolLibFakeArmorStand(sm);
            return;
        }
        this.fakeArmorStand = new NmsFakeArmorStand();
    }

    public void newlyInRange() {
        tagVisible = true;
        if (sm.getMainConfig().getConfig().isUseArmorStand()) {
            fakeArmorStand.spawnFakeArmorStand(player, stackEntity.getEntity(), stackEntity.getEntity().getLocation(), stackEntity.getTag().getDisplayName());
            return;
        }
        sendPacket(stackEntity.getEntity(), player, true);
    }

    public void playerInRange() {
        if (sm.getMainConfig().getConfig().isTagNearbyRayTrace() && !rayTrace((Mob) stackEntity.getEntity(), player)) {
            if (tagVisible) {
                playerOutRange();
            }
            return;
        }
        if (!tagVisible) {
            newlyInRange();
        }
    }

    public void teleportTag() {
        if (!sm.getMainConfig().getConfig().isUseArmorStand()) {
            return;
        }
        fakeArmorStand.teleport(player, stackEntity.getEntity());
    }

    public void playerOutRange() {
        sendPacket(stackEntity.getEntity(), player, false);
        tagVisible = false;
        if (sm.getMainConfig().getConfig().isUseArmorStand()) {
            fakeArmorStand.removeFakeArmorStand(player);
        }
    }

    private void sendPacket(Entity entity, Player player, boolean tagVisible) {
        if (Utilities.getMinecraftVersion() != Utilities.NMS_VERSION) {
            ProtocolLibHook protocolLibHook = sm.getHookManager().getProtocolLibHook();
            if (protocolLibHook == null) {
                return;
            }
            protocolLibHook.sendPacket(player, entity, tagVisible);
            return;
        }
        NMSHelper.sendVisibilityPacket(player, entity, tagVisible);
    }

    private boolean rayTrace(Mob entity, Player player) {
        Vector resultant = entity.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector());
        double distance = player.getEyeLocation().distance(entity.getEyeLocation());
        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), resultant, distance, FluidCollisionMode.NEVER, true);
        return result == null || result.getHitBlock() == null;
    }

}
