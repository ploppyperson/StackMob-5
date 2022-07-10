package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.packets.PlayerManager;
import uk.antiperson.stackmob.packets.PlayerWatcher;

import java.util.List;

public class TagCheckTask extends BukkitRunnable {

    private final StackMob sm;
    public TagCheckTask(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerWatcher playerWatcher = sm.getPlayerManager().getPlayerWatcher(player);
            playerWatcher.checkPlayer();
        }
    }



}
