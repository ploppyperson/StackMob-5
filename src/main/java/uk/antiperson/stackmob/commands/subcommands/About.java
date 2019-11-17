package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.command.CommandSender;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;

@CommandMetadata(command = "about", playerReq = false, desc = "Show information about the plugin.")
public class About extends SubCommand {

    private StackMob sm;
    public About(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("StackMob version " + sm.getDescription().getVersion());
        sender.sendMessage("Report bugs/missing features at https://github.com/Nathat23/StackMob-5");
        return false;
    }
}
