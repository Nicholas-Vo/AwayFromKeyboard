package awayFromKeyboard.commands;

import awayFromKeyboard.utils.Messages;
import org.bukkit.command.CommandSender;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.SubCommand;

public class HelpCommand extends SubCommand {

	public HelpCommand(AwayFromKeyboard afk) {
		super(afk, "help");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		Messages.displayCommandMenu(sender);
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
