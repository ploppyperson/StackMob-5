package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;
import uk.antiperson.stackmob.commands.User;

@CommandMetadata(command = "tool", playerReq = true, desc = "Gives you the stacking tool.")
public class GiveTool extends SubCommand {

    private final StackMob sm;

    public GiveTool(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(User sender, String[] args) {
        sm.getItemTools().giveStackingTool((Player) sender.getSender());
        sender.sendInfo("The stacking tool has been added to your inventory.");
        return false;
    }
}
