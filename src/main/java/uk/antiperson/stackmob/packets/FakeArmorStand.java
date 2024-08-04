package uk.antiperson.stackmob.packets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.utils.Utilities;

public interface FakeArmorStand {

    void spawnFakeArmorStand(Entity owner, Location location, Component name);

    void updateName(Component newName);

    void teleport(Entity owner, Location location);

    void removeFakeArmorStand();


}
