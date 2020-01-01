package uk.antiperson.stackmob.commands;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandArgument {

    private ArgumentType type;
    private boolean optional;
    private List<String> expectedArguments;
    public CommandArgument(ArgumentType type, boolean optional, List<String> expectedArguments) {
        this.type = type;
        this.optional = optional;
        this.expectedArguments = expectedArguments;
    }

    public ArgumentType getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public List<String> getExpectedArguments() {
        if (expectedArguments != null) {
            return expectedArguments;
        }
        ArrayList<String> strings = new ArrayList<>();
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
        return strings;
    }

    public static CommandArgument construct(ArgumentType type) {
        return new CommandArgument(type, false, null);
    }

    public static CommandArgument construct(ArgumentType type, boolean optional) {
        return new CommandArgument(type, optional, null);
    }

    public static CommandArgument construct(ArgumentType type, boolean optional, List<String> expectedArguments) {
        return new CommandArgument(type, optional, expectedArguments);
    }

}
