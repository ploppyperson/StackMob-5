package uk.antiperson.stackmob.commands;

import net.md_5.bungee.api.ChatColor;

public abstract class SubCommand implements Command {

    private final CommandArgument[] arguments;
    public SubCommand(CommandArgument... arguments) {
        this.arguments = arguments;
    }

    public CommandArgument[] getArguments() {
        return arguments;
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

    private CommandMetadata getCommandMetadata() {
        return getClass().getAnnotation(CommandMetadata.class);
    }

    public String buildString(String cmd) {
        StringBuilder args = new StringBuilder();
        for (CommandArgument argumentType : getArguments()) {
            String options = argumentType.buildString();
            if (argumentType.isOptional()) {
                args.append("(").append(options).append(") ");
                continue;
            }
            args.append("[").append(options).append("] ");
        }
        return ChatColor.of("#3CB371") + "/" + cmd + " " + getCommand() + " " + args + ChatColor.GRAY + "- " + ChatColor.of("#90EE90") + getDescription();
    }

}
