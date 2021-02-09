package uk.antiperson.stackmob.tasks;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntCollection;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.Set;

public class MergeTask extends BukkitRunnable {

    private final StackMob sm;

    public MergeTask(StackMob sm) {
        this.sm = sm;
    }

    public void run() {
        final IntCollection toRemove = new IntOpenHashSet();
        originals:
        for (StackEntity original : sm.getEntityManager().getStackEntities()) {
            if (toRemove.contains(original.getEntity().getEntityId())) {
                continue;
            }
            if (original.isWaiting()) {
                original.incrementWait();
                continue;
            }
            if (!original.canStack()) {
                if (!original.getEntity().isValid()) {
                    toRemove.add(original.getEntity().getEntityId());
                }
                continue;
            }
            final Integer[] searchRadius = sm.getMainConfig().getStackRadius(original.getEntity().getType());
            final Set<StackEntity> matches = new ObjectOpenHashSet<>();
            for (Entity nearby : original.getEntity().getNearbyEntities(searchRadius[0], searchRadius[1], searchRadius[2])) {
                if (!(nearby instanceof Mob)) {
                    continue;
                }
                if (!sm.getEntityManager().isStackedEntity((LivingEntity) nearby)) {
                    continue;
                }
                final StackEntity nearbyStack = sm.getEntityManager().getStackEntity((LivingEntity) nearby);
                if (nearbyStack == null) {
                    continue;
                }
                if (toRemove.contains(nearbyStack.getEntity().getEntityId())) {
                    continue;
                }
                if (!nearbyStack.canStack()) {
                    if (!original.getEntity().isValid()) {
                        toRemove.add(original.getEntity().getEntityId());
                    }
                    continue;
                }
                if (!original.match(nearbyStack)) {
                    continue;
                }
                if (nearbyStack.getSize() > 1 || original.getSize() > 1) {
                    final StackEntity removed = nearbyStack.merge(original, false);
                    if (removed != null) {
                        toRemove.add(removed.getEntity().getEntityId());
                        if (original == removed) {
                            continue originals;
                        }
                        break;
                    }
                    continue;
                }
                matches.add(nearbyStack);
            }
            if (!sm.getMainConfig().getStackThresholdEnabled(original.getEntity().getType())) {
                matches.clear();
                continue;
            }
            final int threshold = sm.getMainConfig().getStackThreshold(original.getEntity().getType()) - 1;
            final int size = matches.size();
            if (size < threshold) {
                matches.clear();
                continue;
            }
            for (StackEntity match : matches) {
                match.remove(false);
                toRemove.add(match.getEntity().getEntityId());
            }
            matches.clear();
            if (size + original.getSize() > original.getMaxSize()) {
                final int toCompleteStack = (original.getMaxSize() - original.getSize());
                original.incrementSize(toCompleteStack);
                for (int stackSize : Utilities.split(size - toCompleteStack, original.getMaxSize())) {
                    original.duplicate(stackSize);
                }
                return;
            }
            original.incrementSize(size);
        }
        for (int stackEntity : toRemove) {
            sm.getEntityManager().unregisterStackedEntity(stackEntity);
        }
    }

}
