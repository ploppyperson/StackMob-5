package uk.antiperson.stackmob.packets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import uk.antiperson.stackmob.StackMob;

public class DisplayEntity implements FakeArmorStand {

    private Display display;
    private final StackMob sm;

    public DisplayEntity(StackMob sm) {
        this.sm = sm;
    }


    @Override
    public void spawnFakeArmorStand(Entity owner, Location location, Component name, double offset) {
        display = (Display) owner.getWorld().spawnEntity(adjustLocation(owner, offset), EntityType.TEXT_DISPLAY);
        display.setDisplayHeight(50);
        display.setDisplayWidth(100);
        display.setCustomNameVisible(true);
        display.setBillboard(Display.Billboard.CENTER);
        display.setViewRange(50);
        updateName(name);
    }

    @Override
    public void updateName(Component newName) {
        display.setCustomName(LegacyComponentSerializer.legacySection().serialize(newName));
    }

    @Override
    public void teleport(Entity entity, double offset) {
        display.teleport(adjustLocation(entity, offset));
    }

    @Override
    public void removeFakeArmorStand() {
        display.remove();
    }
}
