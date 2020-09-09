package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.Material;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

import java.util.Arrays;
import java.util.List;

@TraitMetadata(entity = Drowned.class, path = "drowned-hand-item")
public class DrownedItem implements Trait {

    private final List<Material> materials = Arrays.asList(Material.NAUTILUS_SHELL, Material.TRIDENT);
    
    @Override
    public boolean checkTrait (LivingEntity first, LivingEntity nearby) {
        Drowned oriDrowned = (Drowned) first;
        Drowned nearDrowned = (Drowned) nearby;
        EntityEquipment oriEquipment = oriDrowned.getEquipment();
        EntityEquipment nearEquipment = nearDrowned.getEquipment();
        if (oriEquipment == null && nearEquipment == null) return true;
        if (oriEquipment == null || nearEquipment == null) return false;
        if (oriEquipment.getItemInMainHand().getType() == nearEquipment.getItemInMainHand().getType()) {
            return materials.contains(oriEquipment.getItemInMainHand().getType());
        }
        if (oriEquipment.getItemInOffHand().getType() == nearEquipment.getItemInOffHand().getType()) {
            return materials.contains(oriEquipment.getItemInOffHand().getType());
        }
        return false;
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        Drowned oriDrowned = (Drowned) dead;
        Drowned spawnDrowned = (Drowned) spawned;
        if(materials.contains(oriDrowned.getEquipment().getItemInMainHand().getType())){
            spawnDrowned.getEquipment().setItemInMainHand(oriDrowned.getEquipment().getItemInMainHand());
        }
        if(materials.contains(oriDrowned.getEquipment().getItemInOffHand().getType())){
            spawnDrowned.getEquipment().setItemInOffHand(oriDrowned.getEquipment().getItemInOffHand());
        }
    }
}
