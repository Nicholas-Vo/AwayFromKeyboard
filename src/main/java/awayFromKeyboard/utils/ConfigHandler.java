package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private static final FileConfiguration theConfig = AwayFromKeyboard.thePlugin.getConfig();
    public static long timeBeforeMarkedAFK = theConfig.getLong("afkTime");
    public static boolean shouldNotifyConsole = theConfig.getBoolean("consoleNotifications");
    public static boolean announceWhenKickingPlayers = theConfig.getBoolean("announceWhenKickingPlayers");
    public static boolean displayTabListTag = theConfig.getBoolean("displayTabListTag");
    public static boolean announcePlayerNowAfk = theConfig.getBoolean("announcePlayerNowAfk");
    public static boolean announcePlayerNoLongerAfk = theConfig.getBoolean("announcePlayerNoLongerAfk");
    private static Map<String, String> messageMap = new HashMap<>();

    private static AwayFromKeyboard plugin = AwayFromKeyboard.thePlugin;

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
        theConfig.addDefault("displayTabListTag", true);
        theConfig.addDefault("announceWhenKickingPlayers", true);
        theConfig.addDefault("announcePlayerNowAfk", true);
        theConfig.addDefault("announcePlayerNoLongerAfk", true);

        // TODO fix bug where comments don't show up within config

        plugin.saveDefaultConfig();

        ConfigurationSection section = theConfig.getConfigurationSection("messages");
        section.getKeys(false).forEach(key -> {
            String theString = section.getString(key);
            messageMap.put(key, ChatColor.translateAlternateColorCodes('&', theString));
        });

    }

    public static void setConfigurationSetting(String path, String theValue) {
        theConfig.set(path, theValue);
        plugin.saveConfig();
    }

    public static void reloadConfiguration() {
        plugin.reloadConfig();
    }

    public static Map<String, String> getMessageMap() {
        return messageMap;
    }

    // why is translateAltCodes in addDefault? needed?
    private void addDefaultMessage(String path, String message) {
        theConfig.addDefault("messages." + path, "'" + message + "'");
    }

}
