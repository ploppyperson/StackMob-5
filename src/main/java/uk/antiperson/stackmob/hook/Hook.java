package uk.antiperson.stackmob.hook;

import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.StackMob;

public abstract class Hook implements IHook {

    public final StackMob sm;
    private final HookMetadata hookMetadata;
    public Hook(StackMob sm) {
        this.sm = sm;
        this.hookMetadata = getClass().getAnnotation(HookMetadata.class);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onLoad() {

    }

    /**
     * Get the config path for this hook.
     * @return the config path for this hook.
     */
    public String getConfig() {
        return "hooks." + hookMetadata.config();
    }

    /**
     * Gets the plugin for this hook.
     * @return the plugin for this hook.
     */
    public Plugin getPlugin() {
        return sm.getServer().getPluginManager().getPlugin(hookMetadata.name());
    }

}
