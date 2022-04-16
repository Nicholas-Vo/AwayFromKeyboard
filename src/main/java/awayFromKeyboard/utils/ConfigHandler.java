package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void rebuildConfiguration() {
        plugin.saveDefaultConfig();

        theConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

        rebuildSettings();
    }

    public String get(String path) {
        String theString = theConfig.getString("messages." + path);
        return ChatColor.translateAlternateColorCodes('&', theString);
    }

    public boolean getBool(String path) {
        return theConfig.getBoolean(path);
    }

    public List<String> getIgnoredCommands() { return ignoredCommands; }

    public List<String> getIdleTriggerMessages() { return idleTriggerMessages; }
}
