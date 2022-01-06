package awayFromKeyboard.utils;

import org.bukkit.command.CommandSender;

public class Utils {

    public static void sendFormattedMessage(CommandSender sender, String message) {
        sender.sendMessage(message.replaceAll("%playername%", sender.getName()));
    }

    public static int parseNumber(CommandSender sender, String number, String error) {
        int result = 0;
        try {
            result = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            sender.sendMessage(error);
        }
        return result;
    }

}
