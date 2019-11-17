package uk.antiperson.stackmob.commands;

import java.util.List;

public abstract class SubCommand implements Command {

    private ArgumentType[] arguments;
    public SubCommand(ArgumentType... arguments) {
        this.arguments = arguments;
    }

    public ArgumentType[] getArguments() {
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

    @Override
    public List<String> onTabComplete(String arg, int id) {
        return null;
    }

    enum ArgumentType {
        BOOLEAN,
        STRING,
        INTEGER,
        ENTITY_TYPE
    }

}
