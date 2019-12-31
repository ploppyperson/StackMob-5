package uk.antiperson.stackmob.commands;

import java.util.List;

public interface Command {

    boolean onCommand(User sender, String[] args);

    List<String> onTabComplete(String arg, int id);
}
