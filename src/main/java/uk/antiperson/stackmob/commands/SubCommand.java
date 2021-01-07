package uk.antiperson.stackmob.commands;

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

}
