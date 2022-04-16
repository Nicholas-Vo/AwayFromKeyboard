package awayFromKeyboard;

import awayFromKeyboard.commands.KickAllCommand;
import awayFromKeyboard.commands.ListCommand;
import awayFromKeyboard.commands.ReloadCommand;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class AwayFromKeyboard extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
    public static final List<SubCommand> commands = new ArrayList<>();
    public Map<UUID, IdlePlayer> idlePlayerMap = new HashMap<>();
    private ConfigHandler config;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        config = new ConfigHandler(this);

        new Listeners(this);

        commands.add(new ListCommand(this));
        commands.add(new ReloadCommand(this));
        commands.add(new KickAllCommand(this));

        if (config.getBoolean("update-check", true)) {
            new Updater(getLogger(), getDescription().getVersion());
        }

        if (config.getBoolean("metrics", true)) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> new Metrics(this, 14892));
        }
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this); // Shut down any existing tasks
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player player)) {
                displayCommandMenu(sender);
                return true;
            }

            if (!player.hasPermission("afk.goafk")) {
                sendMessage(sender, config.get("noPermission"));
                return true;
            }

            // need to send the player confirmation if there wasn't an announcement message
            if (!config.announcePlayerNowAfk || !player.hasPermission("afk.seenotifications")) {
                sendMessage(player, config.get("markedYourselfAfk"));
            }

            getIdlePlayer(player).setIdle();
            return false;
        }

        String[] restOfArgs = Arrays.copyOfRange(args, 1, args.length);

        commands.forEach(subCommand -> {
            if (subCommand.getName().equals(args[0])) {
                if (sender.hasPermission(subCommand.permission())) {
                    subCommand.executeCommand(sender, restOfArgs);
                } else {
                    sendMessage(sender, config.get("noPermission"));
                }
            }
        });

        return true;
    }

    public IdlePlayer getIdlePlayer(Player player) {
        idlePlayerMap.computeIfAbsent(player.getUniqueId(), x -> new IdlePlayer(player, System.currentTimeMillis()));
        return idlePlayerMap.get(player.getUniqueId());
    }

    public Set<IdlePlayer> getIdlePlayers() {
        return idlePlayerMap.values().stream().filter(IdlePlayer::isIdle).collect(Collectors.toSet());
    }

    public void removeFromIdlePlayerMap(UUID uuid) {
        idlePlayerMap.remove(uuid);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(Chat.formatUsername(sender, message));
    }

    public void broadcast(Player player, String message) {
        Bukkit.broadcast(Chat.formatUsername(player, message), "afk.seenotifications");
    }

    public ConfigHandler config() {
        return config;
    }

    public static AwayFromKeyboard getInstance() {
        return getPlugin(AwayFromKeyboard.class);
    }

    public void displayCommandMenu(CommandSender sender) {
        sender.sendMessage(Chat.title);

        if (sender.hasPermission("afk.goafk")) {
            sender.sendMessage(Chat.arrow + "/afk" + Chat.reset + " - " + "Set yourself AFK");
        }

        AwayFromKeyboard.commands.forEach(command -> {
            if (sender.hasPermission(command.permission())) {
                sender.sendMessage(Chat.arrow + "/afk " + Chat.gray + command.getName()
                        + command.usage() + Chat.reset + " - " + command.description());
            }
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var results = new ArrayList<String>();
        if (args.length == 1) {
            for (String string : getResults(sender)) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(string);
                }
            }
            return results;
        }

        return List.of(); // stop tab completion by returning empty list
    }

    private Set<String> getResults(CommandSender sender) {
        var set = new HashSet<String>();
        commands.forEach(aCommand -> {
            if (sender.hasPermission(aCommand.permission())) {
                set.add(aCommand.getName());
            }
        });
        return set;
    }

}




































