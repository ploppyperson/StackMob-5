package uk.antiperson.stackmob.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CommandHandler {

    private final StackMob sm;
    private final TreeMap<String, SubCommand> subCommands;

    public CommandHandler(StackMob sm) {
        this.sm = sm;
        this.subCommands = new TreeMap<>();
    }

    public void register() {
        // create sub commands
        register(About.class);
        register(SpawnStack.class);
        register(Remove.class);
        register(CheckUpdate.class);
        register(Upgrade.class);
        register(GiveTool.class);
        register(Reload.class);
        register(ForceStack.class);
        register(Stats.class);
        // actually register
        sm.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> cmd = Commands.literal("stackmob");
            cmd.executes(this::onCommand);
            for (SubCommand subCommand : subCommands.values()) {
                LiteralArgumentBuilder<CommandSourceStack> subCmd = subCommand.getSubCmd();
                for (CommandArgument argument : subCommand.getArguments()) {
                    subCmd.then(Commands.argument(argument.getName(), argument.getType()));
                }
                cmd.then(subCmd);
            }
            LiteralCommandNode<CommandSourceStack> stack = cmd.build();
            commands.registrar().register(stack);
        });
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

    public int onCommand(CommandContext<CommandSourceStack> ctx) {
        String cmd = ctx.getInput();
        CommandSender sender = ctx.getSource().getSender();
        Component commands = Utilities.PREFIX.append(Component.text("Commands:").color(TextColor.color(255, 127, 80)));
        sender.sendMessage(commands);
        for (SubCommand subCommand : subCommands.values()) {
            sender.sendMessage(subCommand.buildComponent(cmd));
        }
        Component key = Component.text("Key: () = Optional argument, [] = Mandatory argument.").color(TextColor.color(255, 127, 80));
        sender.sendMessage(key);
        /*SubCommand subCommand = subCommands.get(strings[0].toLowerCase());
        if (subCommand == null) {
            sendError(sender, "Invalid subcommand!");
            return false;
        }
        if (subCommand.isPlayerRequired() && !(sender instanceof Player)) {
            sendError(sender, "This subcommand requires a player!");
            return false;
        }
        String[] subCmdArgs = Utilities.removeFirst(strings);
        if (!validateArgs(subCommand.getArguments(), subCmdArgs)) {
            sendError(sender, "Invalid arguments for '" + subCommand.getCommand() + "'. Usage:");
            sender.sendMessage(subCommand.buildComponent(cmd));
            return false;
        }*/
        //subCommand.onCommand(new User(sender, sender), subCmdArgs);
        return SINGLE_SUCCESS;
    }

    private void sendError(Audience audience, String message) {
        Component noPerm = Utilities.PREFIX.append(Component.text(message).color(NamedTextColor.RED));
        audience.sendMessage(noPerm);
    }
}
