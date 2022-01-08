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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AwayFromKeyboard extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
    public static final String VERSION = "2.0";
    public static final List<SubCommand> commands = new ArrayList<>();
    public static Map<UUID, IdlePlayer> idleMap = new ConcurrentHashMap<>();
    public static AwayFromKeyboard thePlugin;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        thePlugin = this;

        new Listeners(this);
        new ConfigHandler();

        commands.add(new ListCommand(this));
        commands.add(new SetTimeCommand(this));
        commands.add(new ReloadCommand(this));
        commands.add(new KickAllCommand(this));
        commands.add(new HelpCommand(this));

        getLogger().info("Successfully enabled AwayFromKeyboard v" + VERSION + ".");
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this); // Shut down any existing tasks
        getLogger().info("Disabled AwayFromKeyboard " + VERSION + ".");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player player)) {
                Messages.displayCommandMenu(sender);
                return true;
            }

            if (!player.hasPermission("afk.goafk")) {
                sendMessage(sender, Messages.noPermission);
                return true;
            }

            // need to send the player confirmation if there wasn't an announcement message
            if (!ConfigHandler.announcePlayerNowAfk || !player.hasPermission("afk.seenotifications")) {
                sendMessage(player, Messages.markedYourselfAfk);
            }

            IdlePlayer thePlayer = getIdlePlayer(player);

            if (thePlayer.isIdle()) {
                player.sendMessage(Chat.red + "You are already set as idle.");
                return true;
            }

            if (thePlayer.inCooldown()) {
                player.sendMessage(Chat.red + "You are doing that too much. Try again in a little while.");
                return true;
            }

            thePlayer.addToCooldown();
            thePlayer.setIdle();
            return false;
        }

        String[] restOfArgs = Arrays.copyOfRange(args, 1, args.length);

        commands.forEach(subCommand -> {
            if (subCommand.getName().equals(args[0])) {
                if (sender.hasPermission(subCommand.permission())) {
                    subCommand.executeCommand(sender, restOfArgs);
                } else {
                    sendMessage(sender, Messages.noPermission);
                }
            }
        });

        return true;
    }

    public IdlePlayer getIdlePlayer(Player player) {
        idleMap.computeIfAbsent(player.getUniqueId(), k -> new IdlePlayer(player, System.currentTimeMillis()));
        return idleMap.get(player.getUniqueId());
    }

    public Set<IdlePlayer> getIdlePlayers() {
        return idleMap.values().stream().filter(p -> p.isIdle()).collect(Collectors.toSet());
    }

    public static void sendErrorMessage(CommandSender sender, String error) {
        sendMessage(sender, Chat.red + "Error: " + Chat.reset + error);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(Chat.formatUsername(sender, message));
    }

    public static void broadcast(Player player, String message) {
        Bukkit.broadcast(Chat.formatUsername(player, message), "afk.seenotifications");
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




































