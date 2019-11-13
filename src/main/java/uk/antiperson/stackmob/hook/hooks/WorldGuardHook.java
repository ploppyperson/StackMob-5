package uk.antiperson.stackmob.hook.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.Hook;
import uk.antiperson.stackmob.hook.HookMetadata;
import uk.antiperson.stackmob.hook.ProtectionHook;

@HookMetadata(name = "WorldGuard", config = "worldguard")
public class WorldGuardHook extends Hook implements ProtectionHook {

    private static final StateFlag ENTITY_FLAG = new StateFlag("entity-stacking", true);
    public WorldGuardHook(StackMob sm) {
        super(sm);
    }

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(ENTITY_FLAG);
            sm.getLogger().info("Registered WorldGuard region flag.");
        } catch (FlagConflictException e) {
            sm.getLogger().warning("A conflict occurred while trying to register the WorldGuard flag.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean canStack(LivingEntity entity) {
        try {
            RegionQuery rq = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            return rq.testState(BukkitAdapter.adapt(entity.getLocation()), null, ENTITY_FLAG);
        }catch (NullPointerException e){
            return false;
        }
    }

}
