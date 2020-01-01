package uk.antiperson.stackmob.commands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.subcommands.*;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.*;

public class Commands implements CommandExecutor, TabCompleter {

    private StackMob sm;
    private Set<SubCommand> subCommands;
    public Commands(StackMob sm) {
        this.sm = sm;
        this.subCommands = new HashSet<>();
    }

    public void registerSubCommands() {
        subCommands.add(new About(sm));
        subCommands.add(new SpawnStack(sm));
        subCommands.add(new Remove(sm));
        subCommands.add(new CheckUpdate(sm));
        subCommands.add(new Upgrade(sm));
        subCommands.add(new GiveTool(sm));
        subCommands.add(new Reload(sm));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender.hasPermission("stackmob.admin"))) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "You do not have permission!");
            return false;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.GOLD + "Commands: ");
            for (SubCommand subCommand : subCommands) {
                StringBuilder args = new StringBuilder();
                for (CommandArgument argumentType : subCommand.getArguments()) {
                    args.append("[").append(argumentType.getType()).append("] ");
                }
                commandSender.sendMessage(ChatColor.AQUA + "/sm " + subCommand.getCommand() + " " + args + ChatColor.GRAY + "- " + ChatColor.YELLOW + subCommand.getDescription());
            }
            return false;
        }
        for (SubCommand subCommand : subCommands) {
            if (!subCommand.getCommand().equalsIgnoreCase(strings[0])) {
                continue;
            }
            if (subCommand.isPlayerRequired() && !(commandSender instanceof Player)) {
                commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "You are not a player!");
                return false;
            }
            if (!validateArgs(subCommand.getArguments(), (String[]) ArrayUtils.remove(strings, 0))) {
                commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "Invalid arguments!");
                return false;
            }
            subCommand.onCommand(new User(commandSender), (String[]) ArrayUtils.remove(strings, 0));
        }
        return false;
    }

    public boolean validateArgs(CommandArgument[] argumentTypes, String[] args) {
        if (args.length < argumentTypes.length) {
            if (argumentTypes.length == (args.length + 1)) {
                CommandArgument argument = argumentTypes[argumentTypes.length - 1];
                return argument.isOptional();
            }
            return false;
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            CommandArgument argument = argumentTypes[i];
            switch (argument.getType()) {
                case BOOLEAN:
                    if (!(args[i].equals("true") || args[i+1].equals("false"))) return false;
                    break;
                case INTEGER:
                    try {
                        Integer.valueOf(args[i]);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    break;
                case ENTITY_TYPE:
                    try {
                        EntityType.valueOf(args[i]);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
            }
        }
        return true;
    }

    private List<String> getApplicableArgs(ArgumentType type) {
        List<String> strings = new ArrayList<>();
        switch (type) {
            case ENTITY_TYPE:
                for (EntityType etype : EntityType.values()) {
                    if (etype.getEntityClass() == null) {
                        continue;
                    }
                    if (!Mob.class.isAssignableFrom(etype.getEntityClass())) {
                        continue;
                    }
                    strings.add(etype.toString());
                }
                return strings;
            case BOOLEAN:
                return Arrays.asList("true", "false");
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            List<String> args = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                args.add(subCommand.getCommand());
            }
            return args;
        }
        for (SubCommand subCommand : subCommands) {
            if (!subCommand.getCommand().equalsIgnoreCase(strings[0])) {
                continue;
            }
            if (subCommand.getArguments().length < strings.length - 1) {
                return null;
            }
            List<String> args = subCommand.onTabComplete(strings[strings.length - 2], strings.length - 2);
            if (args == null) {
                args = getApplicableArgs(subCommand.getArguments()[strings.length - 2].getType());
            }
            return args;
        }
        return null;
    }
}
