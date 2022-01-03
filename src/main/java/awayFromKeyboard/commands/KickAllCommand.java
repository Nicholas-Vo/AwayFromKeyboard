package awayFromKeyboard.commands;

import awayFromKeyboard.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class KickAllCommand extends SubCommand {

	public KickAllCommand(AwayFromKeyboard afk) {
		super(afk, "kickall");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		int players = afk.getAfkPlayers().size();
		if (afk.getAfkPlayers().isEmpty()) {
			afk.error(sender, "No players are away at the moment.");
			return;
		}

		String playerOrPlayers = players == 1 ? "player" : "players";

		if (args.length == 0) {
			sender.sendMessage("You're about to kick " + ChatColor.RED + players + " " + ChatColor.RESET
					+ playerOrPlayers + ". Are you sure?");
			sender.sendMessage("To confirm, type \"/afk kickall confirm\".");
			return;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
			Notifier.sendMsgToConsole(sender instanceof ConsoleCommandSender
					? "You have" : sender.getName() + " has" + " kicked all AFK players.");

			afk.getAfkPlayers().forEach(player -> {
				player.kickPlayer(Messages.messageToKickedPlayers);
				player.setAfk(false);
			});

			// TODO: Delay this message?
			Notifier.broadcast(Messages.announcementToServer);
		}
	}

	@Override
	public String description() {
		return "Kick all AFK players from the server.";
	}

	@Override
	public String usage() {
		return "";
	}

	@Override
	public String permission() {
		return "afk.kickall";
	}

}
