package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class Messages {
    private static final Map<String, String> msgMap = ConfigHandler.getMessageMap();
    public static String markedYourselfAfk = msgMap.get("markedYourselfAfk");
    public static String isNowAfk = msgMap.get("isNowAfk");
    public static String noLongerAfk = msgMap.get("noLongerAfk");
    public static String announcementToServer = msgMap.get("announcementToServer");
    public static String messageToKickedPlayers = msgMap.get("messageToKickedPlayers");
    public static String tabListTag = msgMap.get("tabListTag");
    public static String noPermission = msgMap.get("noPermission");

    public static String pluginTag = Chat.red + "[AwayFromKeyboard]" + Chat.reset;

    public static void displayCommandMenu(CommandSender sender) {
        String arrow = Chat.gray + "> " + Chat.darkGray;
        sender.sendMessage(arrow + Chat.green + "AwayFromKeyboard Usage");

        if (sender.hasPermission("afk.goafk"))
            sender.sendMessage(arrow + "/afk" + Chat.reset + " - " + "Mark yourself as AFK.");

        AwayFromKeyboard.commands.forEach(command -> {
            if (sender.hasPermission(command.permission())) {
                sender.sendMessage(arrow + "/afk " + Chat.gray + command.getName()
                        + command.usage() + Chat.reset + " - " + command.description());
            }
        });
    }
}
