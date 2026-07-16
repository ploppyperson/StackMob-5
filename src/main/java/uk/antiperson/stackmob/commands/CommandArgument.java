package uk.antiperson.stackmob.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import uk.antiperson.stackmob.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandArgument {

    private final ArgumentType<?> type;
    private final boolean optional;
    private final List<String> expectedArguments;
    private final String label;
    private final String descriptor;
    private CommandArgument(ArgumentType<?> type, boolean optional, List<String> expectedArguments, String label, String descriptor) {
        this.type = type;
        this.optional = optional;
        this.expectedArguments = expectedArguments;
        this.label = label;
        this.descriptor = descriptor;
    }

    public ArgumentType<?> getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public String buildString() {
        StringBuilder options = new StringBuilder();
        if (expectedArguments != null && expectedArguments.size() <= 3) {
            expectedArguments.forEach(argument -> options.append(argument).append("/"));
            options.deleteCharAt(options.length() - 1);
        } else if (getDescriptor() != null) {
            options.append(getDescriptor());
        } else {
            options.append(Utilities.filter(getLabel()).toLowerCase());
        }
        return options.toString();
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getLabel() {
        return label;
    }

    public static CommandArgument construct(ArgumentType<?> type, boolean optional, String label, String descriptor) {
        return new CommandArgument(type, optional,null, label, descriptor);
    }

}
