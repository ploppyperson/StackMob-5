package uk.antiperson.stackmob.hook.hooks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.Hook;
import uk.antiperson.stackmob.hook.HookMetadata;
import uk.antiperson.stackmob.hook.SpawnHook;

@HookMetadata(name = "mcMMO", config = "mcmmo")
public class McmmoHook extends Hook implements SpawnHook {

    public McmmoHook(StackMob sm) {
        super(sm);
    }

    @Override
    public void onSpawn(LivingEntity spawned) {
        spawned.setMetadata("mcMMO: Spawned Entity", new FixedMetadataValue(getPlugin(), true));
    }
}
