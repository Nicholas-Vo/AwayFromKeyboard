package awayFromKeyboard;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationMessage {
    private final String name;
    private final String theMessage;

    public ConfigurationMessage(ConfigurationSection theYmlSection) {
        name = theYmlSection.getName();
        theMessage =
                ChatColor.translateAlternateColorCodes('&',
                theYmlSection.getCurrentPath().replaceAll("'", ""));
    }

    public String getName() {
        return name;
    }

    public String toString(String playerName) {
        return theMessage.replaceAll("%playername%", playerName);
    }
}
