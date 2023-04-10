package awayFromKeyboard.commands;

import awayFromKeyboard.AwayFromKeyboard;
import awayFromKeyboard.IdlePlayer;
import awayFromKeyboard.SubCommand;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class ListCommand extends SubCommand {
    private final ConfigHandler config;

    public ListCommand(AwayFromKeyboard afk) {
        super(afk, "list");

        this.config = afk.config();
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
		Set<IdlePlayer> list = afk.getIdlePlayers();
        if (list.size() == 0) {
           afk.sendMessage(sender, config.get("noPlayersAreAfk"));
            return;
        }

        afk.sendMessage(sender, config.get("thesePlayersAreAfk"));
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
