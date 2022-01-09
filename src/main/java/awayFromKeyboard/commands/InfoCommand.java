package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.Messages;
import org.bukkit.command.CommandSender;

public class InfoCommand extends SubCommand {

	public InfoCommand(AwayFromKeyboard afk) {
		super(afk, "help");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		sender.sendMessage(Chat.title);
		sender.sendMessage(Chat.arrow + "Author: _NickV");
	}

	@Override
	public String description() {
		return "Display this menu";
	}

	@Override
	public String usage() {
		return "";
	}

	@Override
	public String permission() {
		return "afk.menu";
	}

}
