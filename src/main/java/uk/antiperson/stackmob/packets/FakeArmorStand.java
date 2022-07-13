package uk.antiperson.stackmob.packets;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface FakeArmorStand {

    void spawnFakeArmorStand(Entity owner, Location location, Component name);

    void updateName(Component newName);

    void teleport(Entity entity);

    void removeFakeArmorStand();

    default Location adjustLocation(Entity entity) {
        double adjustment = entity.getCustomName() == null || entity.getCustomName().length() == 0 ? 0.1 : 0.3;
        return entity.getLocation().add(0, entity.getHeight() + adjustment, 0);
    }


}
