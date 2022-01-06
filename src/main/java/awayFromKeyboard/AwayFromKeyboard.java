package awayFromKeyboard;

import awayFromKeyboard.commands.*;
import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AwayFromKeyboard extends JavaPlugin implements Listener, CommandExecutor {
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

        // TODO - add tab completion
        // TODO - add configurable delay to /afk kickall

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
                sender.sendMessage(Messages.noPermission);
                return true;
            }

            sender.sendMessage(Messages.markedYourselfAfk);
            getIdlePlayer(player).setIdle();
            return true;
        }

        String[] restOfArgs = Arrays.copyOfRange(args, 1, args.length);

        commands.forEach(subCmd -> {
            if (subCmd.getName().equals(args[0])) {
                if (sender.hasPermission(subCmd.permission())) {
                    subCmd.executeCommand(sender, restOfArgs);
                } else {
                    sender.sendMessage(Messages.noPermission);
                }
            }
        });

        return true;
    }

    public IdlePlayer getIdlePlayer(Player player) {
        var theAtomicLong = new AtomicLong(System.currentTimeMillis());
        idleMap.computeIfAbsent(player.getUniqueId(), k -> new IdlePlayer(player, theAtomicLong));
        return idleMap.get(player.getUniqueId());
    }

    public Set<IdlePlayer> getIdlePlayers() {
        return idleMap.values().stream().filter(p -> p.isIdle()).collect(Collectors.toSet());
    }

    public static void sendMsgToConsole(String info) {
        if (!ConfigHandler.shouldNotifyConsole) return;
        AwayFromKeyboard.thePlugin.getLogger().info(info);
    }

    public static void sendErrorMessage(CommandSender sender, String error) {
        sender.sendMessage(Chat.red + "Error: " + Chat.reset + error);
    }

    public static void notify(String message) {
        sendMsgToConsole(message);
        Bukkit.broadcast(message, "afk.seenotifications");
    }

}




































