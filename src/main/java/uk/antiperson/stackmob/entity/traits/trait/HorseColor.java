package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Horse.class, path = "horse-color")
public class HorseColor extends Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return (((Horse) first).getColor() != ((Horse) nearby).getColor()) ||
                (((Horse) first).getStyle() != ((Horse) nearby).getStyle());
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Horse) spawned).setColor(((Horse) dead).getColor());
        ((Horse) spawned).setStyle(((Horse) spawned).getStyle());
    }
}
