package uk.antiperson.stackmob.entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.hook.StackableMobHook;
import uk.antiperson.stackmob.hook.hooks.ProtocolLibHook;
import uk.antiperson.stackmob.utils.NMSHelper;
import uk.antiperson.stackmob.utils.Utilities;

public class Tag {

    private final StackEntity stackEntity;
    private final StackMob sm;

    public Tag(StackMob sm, StackEntity stackEntity) {
        this.stackEntity = stackEntity;
        this.sm = sm;
    }

    public void update() {
        LivingEntity entity = stackEntity.getEntity();
        int threshold = sm.getMainConfig().getTagThreshold(entity.getType());
        if (stackEntity.getSize() <= threshold) {
            entity.setCustomName(null);
            entity.setCustomNameVisible(false);
            return;
        }
        String displayName = sm.getMainConfig().getTagFormat(entity.getType());
        displayName = StringUtils.replace(displayName, "%type%", getEntityName());
        displayName = StringUtils.replace(displayName, "%size%", stackEntity.getSize() + "");
        displayName = Utilities.translateColorCodes(displayName);
        entity.setCustomName(displayName);
        if (sm.getMainConfig().getTagMode(entity.getType()) == TagMode.ALWAYS) {
            entity.setCustomNameVisible(true);
        }
    }

    private String getEntityName() {
        LivingEntity entity = stackEntity.getEntity();
        String typeString = sm.getEntityTranslation().getTranslatedName(entity.getType());
        if (typeString != null && typeString.length() > 0) {
            return typeString;
        }
        StackableMobHook smh = sm.getHookManager().getApplicableHook(stackEntity);
        typeString = smh != null ? smh.getDisplayName(entity) : entity.getType().toString();
        typeString = typeString == null ? entity.getType().toString() : typeString;
        return WordUtils.capitalizeFully(typeString.replaceAll("[^A-Za-z0-9]", " "));
    }

    public void sendPacket(Player player, boolean tagVisible) {
        if (!Utilities.isNativeVersion()) {
            ProtocolLibHook protocolLibHook = sm.getHookManager().getProtocolLibHook();
            if (protocolLibHook == null) {
                return;
            }
            protocolLibHook.sendPacket(player, stackEntity.getEntity(), tagVisible);
            return;
        }
        NMSHelper.sendPacket(player, stackEntity.getEntity(), tagVisible);
    }

}
