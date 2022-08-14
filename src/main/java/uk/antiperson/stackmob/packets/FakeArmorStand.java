package uk.antiperson.stackmob.packets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.utils.Utilities;

public interface FakeArmorStand {

    void spawnFakeArmorStand(Entity owner, Location location, Component name);

    void updateName(Component newName);

    void teleport(Entity entity);

    void removeFakeArmorStand();

    default Location adjustLocation(Entity entity) {
        double adjustment = shouldAdjust(entity) ? 0.3 : 0.1;
        return entity.getLocation().add(0, entity.getHeight() + adjustment, 0);
    }

    private boolean shouldAdjust(Entity entity) {
        if (Utilities.isPaper()) {
            Component component = entity.customName();
            if (!(component instanceof TextComponent)) {
                return false;
            }
            if (((TextComponent) component).content().length() != 0) {
                return true;
            }
        }
        return entity.getCustomName() != null && entity.getCustomName().length() != 0;
    }


}
