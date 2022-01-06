package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class Messages {
    private static Map<String, String> msgMap;

    public static String markedYourselfAfk;
    public static String isNowAfk;
    public static String announcementToServer;
    public static String messageToKickedPlayers;
    public static String noPermission;
    public static String noPlayersAreAFK;
    public static String tabListTag;
    public static String noLongerAfk;
    public static String pluginTag;

    public static void initalizeMessages() {
        msgMap = ConfigHandler.getMessageMap();
        markedYourselfAfk = msgMap.get("markedYourselfAfk");
        isNowAfk = msgMap.get("isNowAfk");
        announcementToServer = msgMap.get("announcementToServer");
        messageToKickedPlayers = msgMap.get("messageToKickedPlayers");
        noPermission = msgMap.get("noPermission");
        noPlayersAreAFK = msgMap.get("noPlayersAreAFK");
        tabListTag = msgMap.get("tabListTag");
        noLongerAfk = msgMap.get("noLongerAfk");
        pluginTag = Chat.red + "[AwayFromKeyboard]" + Chat.reset;
    }

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
