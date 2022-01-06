package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ConfigHandler {
    public static long timeBeforeMarkedAFK;
    public static boolean shouldNotifyConsole;
    public static boolean announceWhenKickingPlayers;
    public static boolean announcePlayerNowAfk;
    public static boolean announcePlayerNoLongerAfk;
    public static boolean displayTabListTag;

    private static Map<String, String> messageMap = new HashMap<>();
    private static List<String> ignoredCommands = new ArrayList<>();
    private static AwayFromKeyboard plugin;
    private static FileConfiguration theConfig;

    public ConfigHandler() {
        plugin = AwayFromKeyboard.thePlugin;
        theConfig = plugin.getConfig();

        addDefaultMessage("markedYourselfAfk", "You marked yourself as AFK.");
        addDefaultMessage("isNowAfk", "%playername% is now AFK.");
        addDefaultMessage("noLongerAfk", "%playername% is no longer AFK.");
        addDefaultMessage("announcementToServer", "&c[Notice] &7All AFK players have been kicked due to poor server performance.");
        addDefaultMessage("messageToKickedPlayers", "All AFK players have been kicked due to poor server performance.");
        addDefaultMessage("tabListTag", "&8AFK");
        addDefaultMessage("noPermission", "&cYou do not have permission to do that.");
        addDefaultMessage("noPlayersAreAfk", "There are no AFK players at the moment.");

        theConfig.addDefault("afkTime", 5);
        theConfig.addDefault("consoleNotifications", true);
        theConfig.addDefault("displayTabListTag", true);
        theConfig.addDefault("announceWhenKickingPlayers", true);
        theConfig.addDefault("announcePlayerNowAfk", true);
        theConfig.addDefault("announcePlayerNoLongerAfk", true);

        theConfig.addDefault("ignoredCommands", new ArrayList<>(Arrays.asList("/afk", "/vanish")));

        // TODO fix bug where comments don't show up within config

        plugin.saveDefaultConfig();

        rebuildConfiguration();
    }

    public static void rebuildSettings() {
        timeBeforeMarkedAFK = theConfig.getLong("afkTime");
        shouldNotifyConsole = theConfig.getBoolean("consoleNotifications");
        announceWhenKickingPlayers = theConfig.getBoolean("announceWhenKickingPlayers");
        announcePlayerNowAfk = theConfig.getBoolean("announcePlayerNowAfk");
        announcePlayerNoLongerAfk = theConfig.getBoolean("announcePlayerNoLongerAfk");
        displayTabListTag = theConfig.getBoolean("displayTabListTag");
        ignoredCommands = theConfig.getStringList("ignoredCommands");
    }

    public static void save() {
        plugin.saveConfig();

        rebuildConfiguration();
    }

    public static void rebuildConfiguration() {
        messageMap.clear();
        plugin.saveDefaultConfig();

        theConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

        ConfigurationSection section = theConfig.getConfigurationSection("messages");
        section.getKeys(false).forEach(key -> {
            String theString = section.getString(key);
            messageMap.put(key, ChatColor.translateAlternateColorCodes('&', theString));
        });

        Messages.rebuild();
        rebuildSettings();
    }

    public static void setConfigurationSetting(String path, String theValue) {
        theConfig.set(path, theValue);
        save();
    }

    public static Map<String, String> getMessageMap() {
        return messageMap;
    }

    public static List<String> getIgnoredCommands() { return ignoredCommands; }

    private void addDefaultMessage(String path, String message) {
        theConfig.addDefault("messages." + path, "'" + message + "'");
    }

}
