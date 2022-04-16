package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.ConfigHandler;
import org.bukkit.command.CommandSender;

import java.io.File;

public class ReloadCommand extends SubCommand {
    private final ConfigHandler config;

    public ReloadCommand(AwayFromKeyboard afk) {
        super(afk, "reload");

        this.config = afk.config();
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        boolean foundConfig = new File(afk.getDataFolder(), "config.yml").exists();

        afk.reloadConfig();
        afk.config().rebuildConfiguration();

        String theMessage = foundConfig ? "Successfully reloaded the configuration."
                : "The configuration file was missing, so a new one was generated.";

        sender.sendMessage(theMessage);
    }

    @Override
    public String description() {
        return "Reload the plugin configuration";
    }

    @Override
    public String usage() {
        return "";
    }

    @Override
    public String permission() {
        return "afk.reload";
    }
}
