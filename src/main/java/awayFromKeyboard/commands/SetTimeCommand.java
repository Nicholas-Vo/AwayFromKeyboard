package awayFromKeyboard.commands;

import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import awayFromKeyboard.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class SetTimeCommand extends SubCommand {

	public SetTimeCommand(AwayFromKeyboard afk) {
		super(afk, "settime");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("Usage: /afk settime <minutes>");
			return;
		}
		
		int input = Utils.parseNumber(sender, args[0], Chat.red + "Error: " + Chat.reset + "That is not a valid number.");

		if (input > 1000 || input < 1) {
			sender.sendMessage(Chat.red + "You can only set the time between 2 and 1000 minutes.");
			return;
		}

		if (input == ConfigHandler.timeBeforeMarkedAFK) {
			sender.sendMessage(Chat.red + "The AFK time is already set to " + input + ".");
			return;
		}

		ConfigHandler.setConfigurationSetting("afkTime", String.valueOf(input));
		Bukkit.broadcast(Messages.pluginTag + sender.getName() + " set the AFK time to " + input + " minutes.",
				"afk.changetime");
	}

	@Override
	public String description() {
		return "Time before a player is marked AFK.";
	}

	@Override
	public String usage() {
		return " <minutes>";
	}

	@Override
	public String permission() {
		return "afk.changetime";
	}

}
