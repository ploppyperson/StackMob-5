package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;

@CommandMetadata(command = "tool", playerReq = true, desc = "Gives you the stacking tool.")
public class GiveTool extends SubCommand {

    private StackMob sm;
    public GiveTool(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sm.getItemTools().giveStackingTool((Player) sender);
        return false;
    }
}
