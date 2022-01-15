package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class Messages {
    private Map<String, String> msgMap;
    private final ConfigHandler configHandler;

    public Messages(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public static String MARKED_YOURSELF_AFK;
    public static String IS_NOW_AFK;
    public static String KICK_ALL_CMD_MESSAGE;
    public static String KICK_ALL_WARNING;
    public static String MSG_TO_KICKED_PLYRS;
    public static String NO_PERMISSION;
    public static String NO_PLAYERS_AFK;
    public static String TAB_LIST_TAG;
    public static String NO_LONGER_AFK;
    public static String THESE_PLAYERS_AFK;
    public static String AUTO_KICK_MESSAGE;
    public static String AUTO_KICK_ANNOUNCE;
    public static String ABOUT_TO_BE_KICKED_WARNING;
    public static String pluginTag;

    public void rebuild() {
        msgMap = configHandler.getMessageMap();
        MARKED_YOURSELF_AFK = msgMap.get("markedYourselfAfk");
        IS_NOW_AFK = msgMap.get("isNowAfk");
        KICK_ALL_CMD_MESSAGE = msgMap.get("kickAllCommandMessage");
        MSG_TO_KICKED_PLYRS = msgMap.get("messageToKickedPlayers");
        NO_PERMISSION = msgMap.get("noPermission");
        NO_PLAYERS_AFK = msgMap.get("noPlayersAreAfk");
        TAB_LIST_TAG = msgMap.get("tabListTag");
        NO_LONGER_AFK = msgMap.get("noLongerAfk");
        THESE_PLAYERS_AFK = msgMap.get("thesePlayersAreAfk");
        AUTO_KICK_MESSAGE = msgMap.get("youHaveBeenAutoKicked");
        AUTO_KICK_ANNOUNCE = msgMap.get("autoKickAnnounce");
        KICK_ALL_WARNING = msgMap.get("kickAllPlayersWarning");
        ABOUT_TO_BE_KICKED_WARNING = msgMap.get("youAreAboutToBeKicked");

        pluginTag = Chat.red + "[AwayFromKeyboard]" + Chat.reset;
    }

    public static void displayCommandMenu(CommandSender sender) {
        sender.sendMessage(Chat.title);

        if (sender.hasPermission("afk.goafk")) {
            sender.sendMessage(Chat.arrow + "/afk" + Chat.reset + " - " + "Set yourself AFK");
        }

        AwayFromKeyboard.commands.forEach(command -> {
            if (sender.hasPermission(command.permission())) {
                sender.sendMessage(Chat.arrow + "/afk " + Chat.gray + command.getName()
                        + command.usage() + Chat.reset + " - " + command.description());
            }
        });
    }
}
