package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.Material;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

import java.util.Arrays;
import java.util.List;

@TraitMetadata(entity = Drowned.class, path = "drowned-hand-item")
public class DrownedItem implements Trait {

    private final List<Material> materials = Arrays.asList(Material.NAUTILUS_SHELL, Material.TRIDENT);
    private final List<EquipmentSlot> slots = Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    
    @Override
    public boolean checkTrait (LivingEntity first, LivingEntity nearby) {
        Drowned oriDrowned = (Drowned) first;
        Drowned nearDrowned = (Drowned) nearby;
        EntityEquipment oriEquipment = oriDrowned.getEquipment();
        EntityEquipment nearEquipment = nearDrowned.getEquipment();
        if (oriEquipment == null && nearEquipment == null) return true;
        if (oriEquipment == null || nearEquipment == null) return false;
        for (EquipmentSlot equipmentSlot : slots) {
            ItemStack oriItemStack = oriEquipment.getItem(equipmentSlot);
            ItemStack nearItemStack = nearEquipment.getItem(equipmentSlot);
            if (!oriItemStack.isSimilar(nearItemStack)) {
                continue;
            }
            if (materials.contains(oriItemStack.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        Drowned oriDrowned = (Drowned) dead;
        Drowned spawnDrowned = (Drowned) spawned;
        for (EquipmentSlot equipmentSlot : slots) {
            ItemStack item = oriDrowned.getEquipment().getItem(equipmentSlot);
            if (materials.contains(item.getType())) {
                spawnDrowned.getEquipment().setItem(equipmentSlot, item);
            }
        }
    }
}
