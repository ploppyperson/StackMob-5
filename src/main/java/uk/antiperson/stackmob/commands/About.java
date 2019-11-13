package uk.antiperson.stackmob.commands;

import org.bukkit.command.CommandSender;
import uk.antiperson.stackmob.StackMob;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class About extends SubCommand {

    public About(StackMob sm) {
        super("about");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("She sells sea shells on the sea shore");
        return false;
    }
}
