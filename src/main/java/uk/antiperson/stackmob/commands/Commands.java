package uk.antiperson.stackmob.commands;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.subcommands.*;
import uk.antiperson.stackmob.utils.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Commands implements CommandExecutor, TabCompleter {

    private final StackMob sm;
    private final TreeMap<String, SubCommand> subCommands;

    public Commands(StackMob sm) {
        this.sm = sm;
        this.subCommands = new TreeMap<>();
    }

    public void registerSubCommands() {
        register(About.class);
        register(SpawnStack.class);
        register(Remove.class);
        register(CheckUpdate.class);
        register(Upgrade.class);
        register(GiveTool.class);
        register(Reload.class);
        register(ForceStack.class);
        register(Stats.class);
    }

    private void register(Class<? extends SubCommand> subCommandClass) {
        SubCommand subCommand;
        try {
            subCommand = subCommandClass.getConstructor(StackMob.class).newInstance(sm);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            sm.getLogger().info("Error occurred while trying to register " + subCommandClass.getSimpleName() + " sub command!");
            e.printStackTrace();
            return;
        }
        subCommands.put(subCommand.getCommand(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender.hasPermission("stackmob.admin"))) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "You do not have permission!");
            return false;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.of("#FF7F50") + "Commands: ");
            for (SubCommand subCommand : subCommands.values()) {
                String cmd = Bukkit.getServer().getPluginCommand("sm").getPlugin().equals(sm) ? "sm" : "stackmob";
                commandSender.sendMessage(subCommand.buildString(cmd));
            }
            commandSender.sendMessage(ChatColor.of("#FF7F50") + "Key: () = Optional argument, [] = Mandatory argument.");
            return false;
        }
        SubCommand subCommand = subCommands.get(strings[0].toLowerCase());
        if (subCommand == null) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "Invalid subcommand!");
            return false;
        }
        if (subCommand.isPlayerRequired() && !(commandSender instanceof Player)) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "You are not a player!");
            return false;
        }
        String[] subCmdArgs = (String[]) ArrayUtils.remove(strings, 0);
        if (!validateArgs(subCommand.getArguments(), subCmdArgs)) {
            commandSender.sendMessage(Utilities.PREFIX + ChatColor.RED + "Invalid arguments for '" + subCommand.getCommand() + "'. Usage:");
            commandSender.sendMessage(subCommand.buildString("stackmob"));
            return false;
        }
        subCommand.onCommand(new User(commandSender), subCmdArgs);
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
                        EntityType.valueOf(args[i].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                    break;
                case WORLD:
                    if (Bukkit.getWorld(args[i]) == null) {
                        return false;
                    }
                    break;
                case STRING:
                    if (!argument.getExpectedArguments().contains(args[i])) {
                        return false;
                    }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Set<String> expectedArguments = getExpectedArguments(strings);
        expectedArguments.removeIf(possibleArgument -> !possibleArgument.toLowerCase().startsWith(strings[strings.length - 1].toLowerCase()));
        return new ArrayList<>(expectedArguments);
    }

    private Set<String> getExpectedArguments(String[] strings) {
        SubCommand subCommand = subCommands.get(strings[0].toLowerCase());
        if (subCommand == null) {
            return new HashSet<>(subCommands.keySet());
        }
        // the subcommand is correct, so length of arguments is length of string array - 1
        int subCmdArgsLength = strings.length - 1;
        // if the command has no arguments or the arguments exceed cmd length
        if (subCmdArgsLength == 0 || subCmdArgsLength > subCommand.getArguments().length) {
            return Collections.emptySet();
        }
        // get the argument at this position in the cmd
        CommandArgument commandArgument = subCommand.getArguments()[subCmdArgsLength - 1];
        return new TreeSet<>(commandArgument.getExpectedArguments());
    }
}
