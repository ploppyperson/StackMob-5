package uk.antiperson.stackmob.packets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface FakeArmorStand {

    void spawnFakeArmorStand(Player player, Entity owner, Location location, String name);

    void teleport(Player player, Entity entity);

    void removeFakeArmorStand(Player player);

    default Location adjustLocation(Entity entity) {
        double adjustment = entity.getCustomName() == null || entity.getCustomName().length() == 0 ? 0.1 : 0.3;
        return entity.getLocation().add(0, entity.getHeight() + adjustment, 0);
    }


}
