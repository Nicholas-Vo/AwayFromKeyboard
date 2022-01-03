package awayFromKeyboard;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class Notifier {
    private static final FileConfiguration theConfig = AwayFromKeyboard.thePlugin.getConfig();

    public static void sendMsgToConsole(String info) {
        if (!ConfigHandler.shouldNotifyConsole) return;

        AwayFromKeyboard.thePlugin.getLogger().info(info);
    }

    public static void broadcast(String theMessage) {
        if (ConfigHandler.announceWhenKickingPlayers) Bukkit.broadcastMessage(theMessage);
    }

    public static void notify(CommandSender sender, String message) {
        sendMsgToConsole(message);
        Bukkit.broadcast(message, "afk.seeNotifications");
    }

}
