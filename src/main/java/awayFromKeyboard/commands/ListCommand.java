package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.IdlePlayer;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.Chat;
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
            sender.sendMessage("There are no AFK players at the moment.");
            return;
        }

        sender.sendMessage(list.size() == 1 ? "One player is currently AFK: " : "AFK Players: ");

        list.forEach(p -> sender.sendMessage("- " + Chat.gray + p.getName() + Chat.reset + ": " + p.timeAfkToString()));
    }

    @Override
    public String description() {
        return "List all afk players.";
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
