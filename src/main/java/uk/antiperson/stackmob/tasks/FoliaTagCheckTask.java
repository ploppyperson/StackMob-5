package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;

// This is a very shitty way to port this task to be Folia compatible, but it works.
public class FoliaTagCheckTask implements Runnable {

    private final StackMob sm;
    public FoliaTagCheckTask(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScheduler().run(sm, scheduledTask -> sm.getPlayerManager().createPlayerWatcher(player).checkPlayer(), () -> {});
        }
    }
}
