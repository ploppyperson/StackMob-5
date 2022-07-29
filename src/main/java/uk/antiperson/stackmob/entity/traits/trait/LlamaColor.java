package uk.antiperson.stackmob.entity.traits.trait;

import org.bukkit.entity.Llama;
import uk.antiperson.stackmob.entity.traits.EntityTrait;
import uk.antiperson.stackmob.entity.traits.TraitMetadata;

@TraitMetadata(path = "llama-color")
public class LlamaColor extends EntityTrait<Llama> {

    @Override
    public boolean checkTrait(Llama first, Llama nearby) {
        return first.getColor() != nearby.getColor();
    }

    @Override
    public void applyTrait(Llama spawned, Llama dead) {
        spawned.setColor(dead.getColor());
    }
}
