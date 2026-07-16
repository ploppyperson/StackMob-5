package uk.antiperson.stackmob.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SubCommand implements SubCommandInterface {

    private final List<CommandArgument> arguments;
    private final LiteralArgumentBuilder<CommandSourceStack> subCmd;

    public SubCommand() {
        this.subCmd = Commands.literal(getCommand());
        this.arguments = new ArrayList<>();
    }

    @Override
    public void construct() {

    }

    public void addCommandArgument(CommandArgument commandArgument) {
        arguments.add(commandArgument);
    }

    public List<CommandArgument> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public String getCommand() {
        return getCommandMetadata().command();
    }

    public String getDescription() {
        return getCommandMetadata().desc();
    }

    public boolean isPlayerRequired() {
        return getCommandMetadata().playerReq();
    }

    public LiteralArgumentBuilder<CommandSourceStack> getSubCmd() {
        return subCmd;
    }

    private CommandMetadata getCommandMetadata() {
        return getClass().getAnnotation(CommandMetadata.class);
    }

    public Component buildComponent(String cmd) {
        StringBuilder args = new StringBuilder();
        for (CommandArgument argumentType : getArguments()) {
            String options = argumentType.buildString();
            if (argumentType.isOptional()) {
                args.append("(").append(options).append(") ");
                continue;
            }
            args.append("[").append(options).append("] ");
        }
        Component label = Component.text("/" + cmd + " " + getCommand() + " " + args).color(TextColor.color(60, 179, 113));
        Component separator = Component.text("- ").color(NamedTextColor.GRAY);
        Component desc = Component.text(getDescription()).color(TextColor.color(144, 238, 144));
        return label.append(separator).append(desc);
    }

}
