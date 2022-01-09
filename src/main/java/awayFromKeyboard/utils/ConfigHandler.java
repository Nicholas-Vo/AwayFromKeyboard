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
    public static long timeBeforeAutoKick;
    public static long kickAllCommandDelay;
    public static long afkCommandBufferTime;

    public static boolean autoKickEnabled;
    public static boolean ShouldWarnBeforeKick;
    public static boolean shouldDisplayTabListTag;
    public static boolean setPlayerAfkViaChatMessage;

    public static boolean announceWhenKickingPlayers;
    public static boolean announcePlayerNowAfk;
    public static boolean announcePlayerNoLongerAfk;
    public static boolean announceAutoKick;

    private static Map<String, String> messageMap = new HashMap<>();
    private static List<String> ignoredCommands = new ArrayList<>();
    private static List<String> idleTriggerMessages = new ArrayList<>();
    private static AwayFromKeyboard plugin;
    private static FileConfiguration theConfig;

    public ConfigHandler() {
        plugin = AwayFromKeyboard.thePlugin;
        theConfig = plugin.getConfig();

        addDefaultMessage("markedYourselfAfk", "You marked yourself as AFK.");
        addDefaultMessage("isNowAfk", "%playername% is now AFK.");
        addDefaultMessage("noLongerAfk", "%playername% is no longer AFK.");
        addDefaultMessage("announcementToServer", "&c[Notice] &rAll AFK players have been kicked.");
        addDefaultMessage("messageToKickedPlayers", "All AFK players have been kicked.");
        addDefaultMessage("tabListTag", "&8AFK");
        addDefaultMessage("noPermission", "&cYou do not have permission to do that.");
        addDefaultMessage("noPlayersAreAfk", "There are no AFK players at the moment.");
        addDefaultMessage("youHaveBeenAutoKicked", "You were idle for too long and have been kicked.");
        addDefaultMessage("autoKickAnnounce", "&c[Notice] &r%playername% was idle for too long and has been kicked.");
        addDefaultMessage("thesePlayersAreAfk", "The following players are currently AFK:");
        addDefaultMessage("youAreAboutToBeKicked", "&c[Notice] &rYou are about to be kicked for idling for too long.");
        addDefaultMessage("kickAllPlayersWarning", "&c[Notice] &rAll AFK players will be kicked from the server in <seconds> seconds.");

        theConfig.addDefault("timeBeforeMarkedAFK", 10);
        theConfig.addDefault("timeBeforeAutoKick", 60);
        theConfig.addDefault("kickAllCommandDelay", 15);
        theConfig.addDefault("afkCommandBufferTime", 10);

        theConfig.addDefault("shouldDisplayTabListTag", true);
        theConfig.addDefault("autoKickEnabled", false);
        theConfig.addDefault("shouldWarnPlayersBeforeAutoKick", true);

        theConfig.addDefault("announceWhenKickingPlayers", true);
        theConfig.addDefault("announcePlayerNowAfk", true);
        theConfig.addDefault("announcePlayerNoLongerAfk", true);
        theConfig.addDefault("announceAutoKick", true);
        theConfig.addDefault("setPlayerAfkViaChatMessage", true);

        theConfig.addDefault("ignoredCommands", new ArrayList<>(Arrays.asList("/afk", "/example")));
        theConfig.addDefault("idleTriggerMessages", new ArrayList<>(Arrays.asList("afk", "brb")));

        plugin.saveDefaultConfig();

        rebuildConfiguration();
    }

    public static void rebuildSettings() {
        timeBeforeMarkedAFK = theConfig.getLong("afkTime") * 1000 * 60;
        timeBeforeAutoKick = theConfig.getLong("timeBeforeAutoKick") * 1000 * 60;
        afkCommandBufferTime = theConfig.getLong("afkCommandBufferTime") * 20;
        kickAllCommandDelay = theConfig.getLong("kickAllCommandDelay") * 20;

        announceWhenKickingPlayers = theConfig.getBoolean("announceWhenKickingPlayers");
        announcePlayerNowAfk = theConfig.getBoolean("announcePlayerNowAfk");
        announcePlayerNoLongerAfk = theConfig.getBoolean("announcePlayerNoLongerAfk");
        shouldDisplayTabListTag = theConfig.getBoolean("shouldDisplayTabListTag");
        autoKickEnabled = theConfig.getBoolean("autoKickEnabled");
        ShouldWarnBeforeKick = theConfig.getBoolean("shouldWarnPlayersBeforeAutoKick");
        setPlayerAfkViaChatMessage = theConfig.getBoolean("setPlayerAfkViaChatMessage");
        announceAutoKick = theConfig.getBoolean("announceAutoKick");

        ignoredCommands = theConfig.getStringList("ignoredCommands");
        idleTriggerMessages = theConfig.getStringList("chatMessagesWhichTriggerAfk");
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

    public static List<String> getIdleTriggerMessages() { return idleTriggerMessages; }

    private void addDefaultMessage(String path, String message) {
        theConfig.addDefault("messages." + path, "'" + message + "'");
    }

}
