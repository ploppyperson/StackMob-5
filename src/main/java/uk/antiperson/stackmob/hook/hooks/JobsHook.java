package uk.antiperson.stackmob.hook.hooks;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.Hook;
import uk.antiperson.stackmob.hook.HookMetadata;

@HookMetadata(name = "Jobs", config = "jobs")
public class JobsHook extends Hook implements Listener {

    private StackMob sm;
    public JobsHook(StackMob sm) {
        super(sm);
        this.sm = sm;
    }

    @Override
    public void onEnable() {
        sm.getServer().getPluginManager().registerEvents(this, sm);
    }

    @EventHandler
    public void onJobsPayment(JobsPrePaymentEvent event) {
        if (event.getLivingEntity() == null) {
            return;
        }
        if (!sm.getEntityManager().isStackedEntity(event.getLivingEntity())) {
            return;
        }
        event.setCancelled(true);
    }
}
