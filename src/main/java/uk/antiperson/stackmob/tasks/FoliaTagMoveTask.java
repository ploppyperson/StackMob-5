package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.packets.PlayerWatcher;

import java.util.ArrayList;

// This is a very shitty way to port this task to be Folia compatible, but it works.
public class FoliaTagMoveTask implements Runnable {

    private final StackMob sm;

    public FoliaTagMoveTask(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public void run() {
        ArrayList<Player> playerArrayList = new ArrayList<>(Bukkit.getOnlinePlayers());
        sm.getScheduler().runTaskAsynchronously(sm, () -> {
            for (Player player : playerArrayList) {
                PlayerWatcher playerWatcher = sm.getPlayerManager().getPlayerWatcher(player);
                if (playerWatcher == null) {
                    continue;
                }
                player.getScheduler().run(sm, scheduledTask -> playerWatcher.updateTagLocations(), () -> {});
            }
        });
    }
}
