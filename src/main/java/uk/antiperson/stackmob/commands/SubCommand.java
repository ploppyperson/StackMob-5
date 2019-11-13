package uk.antiperson.stackmob.commands;

public abstract class SubCommand implements Command {

    private ArgumentType[] arguments;
    private String command;
    private boolean playerRequired;
    public SubCommand(String command, boolean playerRequired, ArgumentType... arguments) {
        this.arguments = arguments;
        this.command = command;
        this.playerRequired = playerRequired;
    }

    public SubCommand(String command, ArgumentType... arguments) {
        this.arguments = arguments;
        this.command = command;
    }

    public ArgumentType[] getArguments() {
        return arguments;
    }

    public String getCommand() {
        return command;
    }

    public boolean isPlayerRequired() {
        return playerRequired;
    }

    enum ArgumentType {
        BOOLEAN,
        STRING,
        INTEGER,
        ENTITY_TYPE
    }

}
