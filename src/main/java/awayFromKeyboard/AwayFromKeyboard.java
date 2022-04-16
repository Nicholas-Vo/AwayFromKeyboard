package awayFromKeyboard;

import awayFromKeyboard.commands.*;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
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
    public static Map<UUID, IdlePlayer> idlePlayerMap = new HashMap<>();
    private ConfigHandler configHandler;
    private Messages messages;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        configHandler = new ConfigHandler(this);
        messages =  new Messages(configHandler);

        new Listeners(this);

        commands.add(new ListCommand(this));
        commands.add(new ReloadCommand(this));
        commands.add(new KickAllCommand(this));
        commands.add(new HelpCommand(this));
        commands.add(new InfoCommand(this));
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this); // Shut down any existing tasks
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player player)) {
                Messages.displayCommandMenu(sender);
                return true;
            }

            if (!player.hasPermission("afk.goafk")) {
                sendMessage(sender, Messages.NO_PERMISSION);
                return true;
            }

            // need to send the player confirmation if there wasn't an announcement message
            if (!configHandler.announcePlayerNowAfk || !player.hasPermission("afk.seenotifications")) {
                sendMessage(player, Messages.MARKED_YOURSELF_AFK);
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
                    sendMessage(sender, Messages.NO_PERMISSION);
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

    public Messages getMessageHandler() { return messages; }

    public ConfigHandler getConfigHandler() { return configHandler; }

    public static AwayFromKeyboard getInstance() {return getPlugin(AwayFromKeyboard.class); }

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




































