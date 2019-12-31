package uk.antiperson.stackmob.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import uk.antiperson.stackmob.utils.Utilities;

public class User {

    private CommandSender sender;
    public User(CommandSender sender) {
        this.sender = sender;
    }

    public void sendRawMessage(String message) {
        sender.sendMessage(message);
    }

    public void sendInfo(String message) {
        sendMessage(MessageType.INFO, message);
    }

    public void sendError(String message) {
        sendMessage(MessageType.ERROR, message);
    }

    public void sendSuccess(String message) {
        sendMessage(MessageType.SUCCESS, message);
    }

    private void sendMessage(MessageType type, String rawMessage) {
        StringBuilder message = new StringBuilder(Utilities.PREFIX);
        switch (type) {
            case INFO:
                message.append(ChatColor.YELLOW);
                break;
            case ERROR:
                message.append(ChatColor.RED);
                break;
            case SUCCESS:
                message.append(ChatColor.GREEN);
                break;
        }
        message.append(rawMessage);
        sender.sendMessage(message.toString());
    }

    enum MessageType {
        INFO,
        ERROR,
        SUCCESS;
    }
}
