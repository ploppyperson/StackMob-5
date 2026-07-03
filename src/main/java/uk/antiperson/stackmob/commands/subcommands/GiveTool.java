package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
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

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        sm.getItemTools().giveStackingTool((Player) sender.getSender());
        sender.sendInfo("The stacking tool has been added to your inventory.");
        return Command.SINGLE_SUCCESS;
    }
}
