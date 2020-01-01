package uk.antiperson.stackmob.commands;

public interface Command {

    boolean onCommand(User sender, String[] args);

}
