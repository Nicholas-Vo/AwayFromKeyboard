package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class ConfigHandler {
    private static final FileConfiguration theConfig = AwayFromKeyboard.thePlugin.getConfig();
    public static long timeBeforeMarkedAFK = theConfig.getLong("afkTime");
    public static boolean shouldNotifyConsole = theConfig.getBoolean("consoleNotifications");
    public static boolean displayTabListTag = theConfig.getBoolean("displayTabListTag");
    public static boolean announceWhenKickingPlayers = theConfig.getBoolean("announceWhenKickingPlayers");
    private static Map<String, String> messageMap;

    public ConfigHandler() {
        addDefaultMessage("markedYourselfAfk", "You marked yourself as AFK.");
        addDefaultMessage("isNowAfk", "%playername% is now AFK.");
        addDefaultMessage("noLongerAfk", "%playername% is no longer AFK.");
        addDefaultMessage("announcementToServer", "&c[Notice] &7All AFK players have been kicked due to poor server performance.");
        addDefaultMessage("messageToKickedPlayers", "All AFK players have been kicked due to poor server performance.");
        addDefaultMessage("tabListTag", "&8AFK");
        addDefaultMessage("noPermission", "&cError: &rYou cannot do that.");
        theConfig.addDefault("afkTime", 5);
        theConfig.addDefault("consoleNotifications", true);
        theConfig.addDefault("announceWhenKickingPlayers", true);
        theConfig.addDefault("displayTabListTag", true);

        theConfig.getStringList("messages").forEach(theString -> {
            ConfigurationSection theSection = theConfig.getConfigurationSection(theString);
            messageMap.put(theSection.getName(), theSection.getCurrentPath());
        });

        AwayFromKeyboard.thePlugin.saveDefaultConfig();
    }

    public static Map<String, String> getMessageMap() {
        return messageMap;
    }

    // why is translateAltCodes in addDefault? needed?
    private void addDefaultMessage(String path, String message) {
        theConfig.addDefault("messages." + path,
                "'" + ChatColor.translateAlternateColorCodes('&', message) + "'");
    }

}
