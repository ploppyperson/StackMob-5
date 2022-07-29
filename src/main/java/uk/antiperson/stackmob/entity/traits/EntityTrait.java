package uk.antiperson.stackmob.entity.traits;

import org.bukkit.entity.LivingEntity;

import java.lang.reflect.ParameterizedType;

public abstract class EntityTrait<T extends LivingEntity> implements Trait<T> {

    private Class<T> clazz;

    public Class<T> getEntityClass() {
        if (clazz == null) {
            clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return clazz;
    }

}
