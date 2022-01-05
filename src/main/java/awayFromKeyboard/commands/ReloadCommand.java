package awayFromKeyboard.commands;

import awayFromKeyboard.utils.Messages;
import org.bukkit.command.CommandSender;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.SubCommand;
import org.bukkit.command.ConsoleCommandSender;

public class ReloadCommand extends SubCommand {

	public ReloadCommand(AwayFromKeyboard afk) {
		super(afk, "reload");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		afk.reloadConfig();
		String theReloader = (sender instanceof ConsoleCommandSender) ? "The console" : sender.getName();
		afk.getLogger().info(theReloader + " reloaded the configuration.");
		sender.sendMessage(Messages.pluginTag + " Successfully reloaded the configuration.");
	}

	@Override
	public String description() {
		return "Reload the plugin.";
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
