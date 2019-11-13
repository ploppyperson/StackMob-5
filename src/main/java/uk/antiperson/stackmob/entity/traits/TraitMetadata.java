package uk.antiperson.stackmob.entity.traits;

import org.bukkit.entity.Mob;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TraitMetadata {
    Class<? extends Mob> entity();
    String path();
}
