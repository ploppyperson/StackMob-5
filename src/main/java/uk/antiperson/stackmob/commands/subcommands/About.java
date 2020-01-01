package uk.antiperson.stackmob.commands.subcommands;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;
import uk.antiperson.stackmob.commands.User;

@CommandMetadata(command = "about", playerReq = false, desc = "Show information about the plugin.")
public class About extends SubCommand {

    private StackMob sm;
    public About(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(User sender, String[] args) {
        sender.sendInfo("StackMob version " + sm.getDescription().getVersion());
        if (sm.getServer().getPluginManager().isPluginEnabled("StackMobBridge")) {
            String bridgeVersion = sm.getServer().getPluginManager().getPlugin("StackMobBridge").getDescription().getVersion();
            sender.sendInfo("StackMobBridge version " + bridgeVersion);
        }
        sender.sendInfo("Please tell us about any feature requests, issues or bugs at https://github.com/Nathat23/StackMob-5");
        return false;
    }
}
