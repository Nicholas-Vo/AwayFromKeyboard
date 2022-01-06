package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.IdlePlayer;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import awayFromKeyboard.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class ListCommand extends SubCommand {

    public ListCommand(AwayFromKeyboard afk) {
        super(afk, "list");
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
		Set<IdlePlayer> list = afk.getIdlePlayers();

        if (list.size() == 0) {
            Utils.sendFormattedMessage(sender, Messages.noPlayersAreAfk);
            return;
        }

        Utils.sendFormattedMessage(sender, Messages.thesePlayersAreAfk);
        sender.sendMessage("AFK Players: ");

        list.forEach(p -> sender.sendMessage("- " + Chat.gray + p.getName() + Chat.reset + ":" + p.timeIdleToString()));
    }

    @Override
    public String description() {
        return "List afk players";
    }

    @Override
    public String usage() {
        return "";
    }

    @Override
    public String permission() {
        return "afk.list";
    }

}
