package uk.antiperson.stackmob.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface SubCommandInterface {

    void construct();

    default int onCommand(CommandContext<CommandSourceStack> ctx, User user) {
        user.sendRawMessage("Default sub command implementation - if you're not the developer this is probably a mistake");
        return Command.SINGLE_SUCCESS;
    }

}
