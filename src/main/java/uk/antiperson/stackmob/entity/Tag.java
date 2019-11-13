package uk.antiperson.stackmob.entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.StackableMobHook;

public class Tag {

    private StackEntity stackEntity;
    private StackMob sm;
    public Tag(StackMob sm, StackEntity stackEntity) {
        this.stackEntity = stackEntity;
        this.sm = sm;
    }

    public void update() {
        LivingEntity entity = stackEntity.getEntity();
        int threshold = sm.getMainConfig().getTagThreshold(entity.getType());
        if (stackEntity.getSize() <= threshold) {
            entity.setCustomName(null);
            return;
        }
        String displayName = sm.getMainConfig().getTagFormat(entity.getType());
        String typeString = entity.getType().toString();
        StackableMobHook smh = sm.getHookManager().getApplicableHook(stackEntity);
        if (smh != null) {
            typeString = smh.getDisplayName(entity);
        }
        String formatted = WordUtils.capitalizeFully(typeString.replaceAll("[^A-Za-z0-9]", " "));
        displayName = StringUtils.replace(displayName, "%type%", formatted);
        displayName = StringUtils.replace(displayName, "%size%", stackEntity.getSize() + "");
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        entity.setCustomName(displayName);
        if (sm.getMainConfig().getTagMode(entity.getType()) == TagMode.ALWAYS) {
            entity.setCustomNameVisible(true);
        }
    }


}
