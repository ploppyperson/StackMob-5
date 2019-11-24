package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.command.CommandSender;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;

@CommandMetadata(command = "upgrade", playerReq = false, desc = "Updates StackMob to the latest version.")
public class Upgrade extends SubCommand {

    private StackMob sm;
    public Upgrade(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Starting download. Please wait...");
        sm.getUpdater().downloadUpdate().whenComplete((downloadResult, throwable) -> {
            switch (downloadResult) {
                case ERROR:
                    sender.sendMessage("An error occurred while checking for updates.");
                    break;
                case SUCCESSFUL:
                    sender.sendMessage("The new update was downloaded successfully!");
                    sender.sendMessage("To apply this update, make sure to restart your server.");
                    break;
            }
        });
        return false;
    }
}
