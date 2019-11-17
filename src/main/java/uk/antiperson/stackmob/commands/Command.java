package uk.antiperson.stackmob.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {

    boolean onCommand(CommandSender sender, String[] args);

    List<String> onTabComplete(String arg, int id);
}
