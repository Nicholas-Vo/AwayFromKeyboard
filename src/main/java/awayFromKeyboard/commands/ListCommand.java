package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.IdlePlayer;
import awayFromKeyboard.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListCommand extends SubCommand {

    public ListCommand(AwayFromKeyboard afk) {
        super(afk, "list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
		Set<IdlePlayer> list = afk.getIdlePlayers();

        if (list.size() == 0) {
            sender.sendMessage("There are no AFK players at the moment.");
            return;
        }

        sender.sendMessage(list.size() == 1 ? "One player is currently AFK: " : "AFK Players: ");

        list.forEach(p -> sender.sendMessage("- " + ChatColor.GRAY + p.getName() + ChatColor.RESET + ": " + p.getTimeAFK()));
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
