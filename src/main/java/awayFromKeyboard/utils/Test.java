package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Test {

    public static void aTest(String message) {
        if (Bukkit.getPlayer("_NickV") != null) {
            Bukkit.getPlayer("_NickV").sendMessage(Chat.red + "[TEST] " + Chat.reset + message);
            AwayFromKeyboard.thePlugin.getLogger().info(Chat.red + "[TEST] " + Chat.reset + message);
        }
    }


}
