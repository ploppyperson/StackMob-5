package uk.antiperson.stackmob.packets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerWatcher {

    private final StackMob sm;
    private final Player player;
    private final Map<UUID, TagHandler> lastRange;

    public PlayerWatcher(StackMob sm, Player player) {
        this.sm = sm;
        this.player = player;
        this.lastRange = new ConcurrentHashMap<>();
    }

    public void checkPlayer() {
        // get nearby entities to player
        Set<StackEntity> nearby = getNearbyStacks();
        Set<TagHandler> remaining = new HashSet<>(lastRange.values());
        for (StackEntity iterated : nearby) {
            if (sm.getMainConfig().getTagMode(iterated.getEntity().getType()) != StackEntity.TagMode.NEARBY) {
                continue;
            }
            int threshold = sm.getMainConfig().getTagThreshold(iterated.getEntity().getType());
            if (iterated.getSize() <= threshold) {
                continue;
            }
            TagHandler tagHandler = lastRange.get(iterated.getEntity().getUniqueId());
            remaining.remove(tagHandler);
            if (tagHandler == null) {
                // entity is not currently being tracked, however is in range, so a packet should be sent
                TagHandler newTagHandler = new TagHandler(sm, player, iterated);
                newTagHandler.init();
                newTagHandler.newlyInRange();
                lastRange.put(iterated.getEntity().getUniqueId(), newTagHandler);
                continue;
            }
            // entity is already tracked and in range
            tagHandler.playerInRange();
        }
        // entities that are not in range
        for (TagHandler wasInRange : remaining) {
            // do packet unsending stuff
            wasInRange.playerOutRange();
        }
    }

    public void stopWatching() {
        for (TagHandler tagHandler : lastRange.values()) {
            tagHandler.playerOutRange();
        }
    }

    public void updateTagLocations() {
        for (TagHandler tagHandler : lastRange.values()) {
            tagHandler.teleportTag();
        }
    }

    private Set<StackEntity> getNearbyStacks() {
        Integer[] searchRadius = sm.getMainConfig().getTagNeabyRadius();
        double searchX = searchRadius[0];
        double searchY = searchRadius[1];
        double searchZ = searchRadius[2];
        HashSet<StackEntity> stackEntities = new HashSet<>();
        for (LivingEntity entity : player.getWorld().getNearbyEntitiesByType(Mob.class, player.getLocation(), searchX, searchY, searchZ)) {
            StackEntity stackEntity = sm.getEntityManager().getStackEntity(entity);
            if (stackEntity == null) {
                continue;
            }
            stackEntities.add(stackEntity);
        }
        return stackEntities;
    }

    public Player getPlayer() {
        return player;
    }
}
