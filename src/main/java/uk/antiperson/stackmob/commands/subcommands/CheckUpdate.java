package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.command.CommandSender;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;

@CommandMetadata(command = "update", playerReq = false, desc = "Check for plugin updates.")
public class CheckUpdate extends SubCommand {

    private StackMob sm;
    public CheckUpdate(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Contacting SpigotMC. Please wait...");
        sm.getUpdater().checkUpdate().whenComplete((updateResult, throwable) -> {
           switch (updateResult.getResult()) {
               case ERROR:
                   sender.sendMessage("An error occurred while checking for updates.");
                   break;
               case NONE:
                   sender.sendMessage("There are no updates currently available.");
                   break;
               case AVAILABLE:
                   sender.sendMessage("A new update (" + updateResult.getNewVersion() + ") is currently available!");
                   sender.sendMessage("This can be downloaded using the command '/sm upgrade'");
                   break;
           }
        });
        return false;
    }
}
