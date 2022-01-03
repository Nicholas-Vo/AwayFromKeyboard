package awayFromKeyboard;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static void displayCommandMenu(CommandSender sender) {
        String arrow = ChatColor.GRAY + "> " + ChatColor.DARK_GRAY;
        sender.sendMessage(arrow + ChatColor.GREEN + "AwayFromKeyboard Usage");

        if (sender.hasPermission("afk.goafk"))
            sender.sendMessage(arrow + "/afk" + ChatColor.RESET + " - " + "Mark yourself as AFK.");

        for (SubCommand cmd : AwayFromKeyboard.getCommandList()) {
            if (sender.hasPermission(cmd.permission())) {
                sender.sendMessage(arrow + "/afk " + ChatColor.GRAY + cmd.getName() + cmd.usage() + ChatColor.WHITE + " - "
                        + cmd.description());
            }
        }
    }
}
