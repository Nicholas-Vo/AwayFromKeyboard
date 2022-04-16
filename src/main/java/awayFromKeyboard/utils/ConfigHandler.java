package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ConfigHandler {
    public long timeBeforeMarkedAFK;
    public long timeBeforeAutoKick;
    public long kickAllCommandDelay;
    public long afkCommandBufferTime;

    public boolean autoKickEnabled;
    public boolean ShouldWarnBeforeKick;
    public boolean shouldDisplayTabListTag;
    public boolean setPlayerAfkViaChatMessage;

    public boolean announceWhenKickingPlayers;
    public boolean announcePlayerNowAfk;
    public boolean announcePlayerNoLongerAfk;
    public boolean announceAutoKick;

    private final Map<String, String> messageMap = new HashMap<>();
    private List<String> ignoredCommands = new ArrayList<>();
    private List<String> idleTriggerMessages = new ArrayList<>();
    private FileConfiguration theConfig;
    private final AwayFromKeyboard plugin;

    public ConfigHandler(AwayFromKeyboard plugin) {
        this.plugin = plugin;
        theConfig = plugin.getConfig();

        plugin.saveDefaultConfig();
        rebuildConfiguration();
    }

    public void rebuildSettings() {
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

    public void save() {
        plugin.saveConfig();

        rebuildConfiguration();
    }

    public void rebuildConfiguration() {
        messageMap.clear();
        plugin.saveDefaultConfig();

        theConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

        ConfigurationSection section = theConfig.getConfigurationSection("messages");

        section.getKeys(false).forEach(key -> {
            String theString = section.getString(key);
            messageMap.put(key, ChatColor.translateAlternateColorCodes('&', theString));
        });

        plugin.getMessageHandler().rebuild();
        rebuildSettings();
    }

    public void setConfigurationSetting(String path, String theValue) {
        theConfig.set(path, theValue);
        save();
    }

    public Map<String, String> getMessageMap() {
        return messageMap;
    }

    public List<String> getIgnoredCommands() { return ignoredCommands; }

    public List<String> getIdleTriggerMessages() { return idleTriggerMessages; }
}
