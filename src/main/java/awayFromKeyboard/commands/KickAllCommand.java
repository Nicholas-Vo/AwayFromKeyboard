package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.IdlePlayer;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class KickAllCommand extends SubCommand {
	private final ConfigHandler config = afk.config();

	public KickAllCommand(AwayFromKeyboard afk) {
		super(afk, "kickall");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		Set<IdlePlayer> theIdle = afk.getIdlePlayers();
		if (theIdle.size() == 0) {
			sender.sendMessage("There aren't any idle players to kick at the moment.");
			return;
		}

		String playerOrPlayers = theIdle.size() == 1 ? "player" : "players";

		if (args.length == 0) {
			sender.sendMessage("You're about to kick " + Chat.red + theIdle.size() + " "
					+ Chat.reset + playerOrPlayers + ". Are you sure?");
			sender.sendMessage("To confirm, type " + Chat.red + "\"/afk kickall confirm\"" + Chat.reset + ".");
			return;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {

			if (config.kickAllCommandDelay > 0) {
				String theAlert = config.get("kickAllPlayersWarning").replaceAll("<seconds>", String.valueOf(config.kickAllCommandDelay / 20));
				Bukkit.broadcastMessage(theAlert);
			}

			Bukkit.getScheduler().runTaskLater(afk, () -> {
				if (theIdle.size() == 0) {
					return;
				}

				theIdle.forEach(player -> {
					if (player != sender && player.isIdle()) {
						player.kickPlayer(config.get("messageToKickedPlayers"));
					}
				});

				if (config.announceWhenKickingPlayers) {
					Bukkit.broadcastMessage(config.get("kickAllCommandMessage"));
				}

			}, config.kickAllCommandDelay);

		}
	}

	@Override
	public String description() {
		return "Kick all AFK players from the server";
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
