package uk.antiperson.stackmob.hook.hooks;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.Hook;
import uk.antiperson.stackmob.hook.HookMetadata;
import uk.antiperson.stackmob.hook.SegregatedMobHook;

@HookMetadata(name = "Citizens", config = "citizens")
public class CitizensHook extends Hook implements SegregatedMobHook {

    public CitizensHook(StackMob sm) {
        super(sm);
    }

    @Override
    public boolean isCustomMob(LivingEntity entity) {
        return entity.hasMetadata("NPC");
    }
}
