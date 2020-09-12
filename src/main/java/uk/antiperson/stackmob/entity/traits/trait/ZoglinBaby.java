package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zoglin;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Zoglin.class, path = "zoglin-baby")
public class ZoglinBaby extends Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Zoglin) first).isBaby() != ((Zoglin) nearby).isBaby();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Zoglin) spawned).setBaby(((Zoglin) dead).isBaby());
    }
}
