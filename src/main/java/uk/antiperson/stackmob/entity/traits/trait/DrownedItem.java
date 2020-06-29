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
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        Drowned oriDrowned = (Drowned) first;
        Drowned nearDrowned = (Drowned) nearby;
        if(materials.contains(oriDrowned.getEquipment().getItemInMainHand().getType()) ||
                materials.contains(nearDrowned.getEquipment().getItemInMainHand().getType())){
            if(oriDrowned.getEquipment().getItemInMainHand().getType() !=
                    nearDrowned.getEquipment().getItemInMainHand().getType()){
                return true;
            }
        }
        if(materials.contains(oriDrowned.getEquipment().getItemInOffHand().getType()) ||
                materials.contains(nearDrowned.getEquipment().getItemInOffHand().getType())){
            return oriDrowned.getEquipment().getItemInOffHand().getType() !=
                    nearDrowned.getEquipment().getItemInOffHand().getType();
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
