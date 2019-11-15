package uk.antiperson.stackmob.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.antiperson.stackmob.StackMob;

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
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender.hasPermission("stackmob.admin"))) {
            commandSender.sendMessage("You do not have permission!");
            return false;
        }
        if (strings.length == 0) {
            commandSender.sendMessage("Commands: ");
            for (SubCommand subCommand : subCommands) {
                StringBuilder args = new StringBuilder();
                for (SubCommand.ArgumentType argumentType : subCommand.getArguments()) {
                    args.append("[").append(argumentType).append("] ");
                }
                commandSender.sendMessage("/sm " + subCommand.getCommand() + " " + args + "- " + subCommand.getDescription());
            }
            return false;
        }
        for (SubCommand subCommand : subCommands) {
            if (!subCommand.getCommand().equalsIgnoreCase(strings[0])) {
                continue;
            }
            if (subCommand.isPlayerRequired() && !(commandSender instanceof Player)) {
                commandSender.sendMessage("You are not a player");
                return false;
            }
            if (!validateArgs(subCommand.getArguments(), strings)) {
                commandSender.sendMessage("Invalid arguments!");
                return false;
            }
            subCommand.onCommand(commandSender, strings);
        }
        /*switch (strings[0]){
            case "remove":
                Function<Entity, Boolean> function;
                switch (strings[1]) {
                    case "animals":
                        function = entity -> entity instanceof Animals;
                        break;
                    case "hostile":
                        function = entity -> entity instanceof Monster;
                        break;
                    default:
                        function = entity -> entity instanceof Mob;
                }
                List<LivingEntity> entities = player.getWorld().getLivingEntities();
                for (LivingEntity entity : entities) {
                    if (!function.apply(entity)) {
                        continue;
                    }
                    if (!sm.getEntityManager().isStackedEntity(entity)) {
                        continue;
                    }
                    entity.remove();
                }
                break;
        }*/
        return false;
    }

    public boolean validateArgs(SubCommand.ArgumentType[] argumentTypes, String[] args) {
        if (args.length - 1 != argumentTypes.length) {
            return false;
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            SubCommand.ArgumentType type = argumentTypes[i];
            switch (type) {
                case BOOLEAN:
                    if (!(args[i + 1].equals("true") || args[i+1].equals("false"))) return false;
                    break;
                case INTEGER:
                    try {
                        Integer.valueOf(args[i + 1]);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    break;
                case ENTITY_TYPE:
                    try {
                        EntityType.valueOf(args[i + 1]);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
            }
        }
        return true;
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
        /*if (!(commandSender instanceof Player)) {
            return null;
        }
        if (!(commandSender.hasPermission("stackmob.admin"))) {
            return null;
        }
        switch (strings.length - 1) {
            case 0:
                return Arrays.asList("about", "remove");
            case 1:
                if (strings[0].equalsIgnoreCase("remove")) {
                    return Arrays.asList("animals", "hostile");
                }
        }*/
        return null;
    }
}
