package uk.antiperson.stackmob.commands;

import org.bukkit.command.CommandSender;

public interface Command {

    boolean onCommand(CommandSender sender, String[] args);
}
