package uk.antiperson.stackmob.hook.hooks;

import com.gamingmesh.jobs.Jobs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.Hook;
import uk.antiperson.stackmob.hook.HookMetadata;
import uk.antiperson.stackmob.hook.SpawnHook;

@HookMetadata(name = "Jobs", config = "jobs")
public class JobsHook extends Hook implements SpawnHook {

    public JobsHook(StackMob sm) {
        super(sm);
    }

    @Override
    public void onSpawn(LivingEntity spawned) {
        spawned.setMetadata(Jobs.getPlayerManager().getMobSpawnerMetadata(), new FixedMetadataValue(getPlugin(), true));
    }
}
