package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class Messages {
    private static Map<String, String> msgMap;

    public static String markedYourselfAfk;
    public static String isNowAfk;
    public static String kickAllCommandMessage;
    public static String kickAllPlayersWarning;
    public static String messageToKickedPlayers;
    public static String noPermission;
    public static String noPlayersAreAfk;
    public static String tabListTag;
    public static String noLongerAfk;
    public static String thesePlayersAreAfk;
    public static String youHaveBeenAutoKicked;
    public static String autoKickAnnounce;
    public static String youAreAboutToBeKicked;

    public static String pluginTag;

    public static void rebuild() {
        msgMap = ConfigHandler.getMessageMap();
        markedYourselfAfk = msgMap.get("markedYourselfAfk");
        isNowAfk = msgMap.get("isNowAfk");
        kickAllCommandMessage = msgMap.get("kickAllCommandMessage");
        messageToKickedPlayers = msgMap.get("messageToKickedPlayers");
        noPermission = msgMap.get("noPermission");
        noPlayersAreAfk = msgMap.get("noPlayersAreAfk");
        tabListTag = msgMap.get("tabListTag");
        noLongerAfk = msgMap.get("noLongerAfk");
        thesePlayersAreAfk = msgMap.get("thesePlayersAreAfk");
        youHaveBeenAutoKicked = msgMap.get("youHaveBeenAutoKicked");
        autoKickAnnounce = msgMap.get("autoKickAnnounce");
        kickAllPlayersWarning = msgMap.get("kickAllPlayersWarning");
        youAreAboutToBeKicked = msgMap.get("youAreAboutToBeKicked");

        pluginTag = Chat.red + "[AwayFromKeyboard]" + Chat.reset;
    }

    public static void displayCommandMenu(CommandSender sender) {
        String arrow = Chat.gray + "> " + Chat.darkGray;
        sender.sendMessage(Chat.green + "AwayFromKeyboard " + Chat.gray + "v" + AwayFromKeyboard.VERSION);

        if (sender.hasPermission("afk.goafk"))
            sender.sendMessage(arrow + "/afk" + Chat.reset + " - " + "Set yourself AFK");

        AwayFromKeyboard.commands.forEach(command -> {
            if (sender.hasPermission(command.permission())) {
                sender.sendMessage(arrow + "/afk " + Chat.gray + command.getName()
                        + command.usage() + Chat.reset + " - " + command.description());
            }
        });
    }
}
