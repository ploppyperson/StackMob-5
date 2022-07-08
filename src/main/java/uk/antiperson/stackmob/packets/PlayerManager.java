package uk.antiperson.stackmob.packets;

import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerWatcher> map;
    private final StackMob sm;

    public PlayerManager(StackMob sm) {
        this.sm = sm;
        this.map = new HashMap<>();
    }

    public PlayerWatcher getPlayerWatcher(Player player) {
        PlayerWatcher playerWatcher = map.get(player.getUniqueId());
        if (playerWatcher == null) {
            playerWatcher = new PlayerWatcher(sm, player);
            map.put(player.getUniqueId(), playerWatcher);
        }
        return playerWatcher;
    }

    public void stopWatching(Player player) {
        PlayerWatcher playerWatcher = map.get(player.getUniqueId());
        playerWatcher.stopWatching();
    }
}
