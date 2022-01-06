package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(AwayFromKeyboard afk) {
        super(afk, "reload");
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        boolean foundConfig = new File(afk.getDataFolder(), "config.yml").exists();

        afk.reloadConfig();
        ConfigHandler.rebuildConfiguration();

        String theMessage = foundConfig ? " Successfully reloaded the configuration."
                : " The configuration file was missing, so a new one has been created.";

        sender.sendMessage(Messages.pluginTag + theMessage);
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
