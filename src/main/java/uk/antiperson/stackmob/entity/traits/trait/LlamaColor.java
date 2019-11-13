package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import uk.antiperson.stackmob.entity.traits.Trait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(entity = Llama.class, path = "llama-color")
public class LlamaColor implements Trait {

    @Override
    public boolean checkTrait(LivingEntity first, LivingEntity nearby) {
        return ((Llama) first).getColor() != ((Llama) nearby).getColor();
    }

    @Override
    public void applyTrait(LivingEntity spawned, LivingEntity dead) {
        ((Llama) spawned).setColor(((Llama) dead).getColor());
    }
}
