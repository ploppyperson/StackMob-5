package uk.antiperson.stackmob.commands;

public class CommandArgument {

    private ArgumentType type;
    private boolean optional;
    public CommandArgument(ArgumentType type, boolean optional) {
        this.type = type;
        this.optional = optional;
    }

    public ArgumentType getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public static CommandArgument construct(ArgumentType type) {
        return new CommandArgument(type, false);
    }

    public static CommandArgument construct(ArgumentType type, boolean optional) {
        return new CommandArgument(type, optional);
    }

}
