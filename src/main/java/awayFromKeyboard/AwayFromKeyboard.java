package awayFromKeyboard;

import java.io.ObjectInputFilter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import awayFromKeyboard.commands.KickAllCommand;
import awayFromKeyboard.commands.ListCommand;
import awayFromKeyboard.commands.ReloadCommand;
import awayFromKeyboard.commands.SetTimeCommand;
import net.md_5.bungee.api.ChatColor;

public class AwayFromKeyboard extends JavaPlugin implements Listener, CommandExecutor {
    private final String VERSION = "2.0";
    private static final List<SubCommand> commands = new ArrayList<>();
    private static Set<UUID> afkPlayers = new LinkedHashSet<>();

    public static AwayFromKeyboard thePlugin;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        thePlugin = this;

        new Listeners(this); // todo move all listeners into individual classes
        new ConfigHandler();

        commands.add(new ListCommand(this));
        commands.add(new SetTimeCommand(this));
        commands.add(new ReloadCommand(this));
        commands.add(new KickAllCommand(this));

        sendMsgToConsole("Enabling AwayFromKeyboard " + VERSION + "...");
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this); // cancel all tasks
       sendMsgToConsole("Disabled AwayFromKeyboard " + VERSION + ".");
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

            // if the player doesn't see the global notifications
            // then we need to send them command confirmation
            if (!player.hasPermission("afk.seeNotifications")) {
                sender.sendMessage(Messages.markedYourselfAfk);
            }

            ((IdlePlayer) player).setIdle();
            return true;
        }

        String[] restOfArgs = Arrays.copyOfRange(args, 1, args.length);

        commands.forEach(subCmd -> {
            if (subCmd.getName().equals(args[0])) {
                if (sender.hasPermission(subCmd.permission())) {
                    subCmd.execute(sender, restOfArgs);
                } else {
                    sender.sendMessage(Messages.noPermission);
                }
            }
        });

        Messages.displayCommandMenu(sender);
        return true;
    }

    public static List<SubCommand> getCommandList() {
        return commands;
    }

    public Set<IdlePlayer> getIdlePlayers() {
        Set<IdlePlayer> idle = new LinkedHashSet<>();
        afkPlayers.forEach(player -> idle.add((IdlePlayer) Bukkit.getPlayer(player)));
        return idle;
    }

    public static void sendMsgToConsole(String info) {
        if (!ConfigHandler.shouldNotifyConsole) return;
        AwayFromKeyboard.thePlugin.getLogger().info(info);
    }

    public static void broadcastMessage(String theMessage) {
        if (ConfigHandler.announceWhenKickingPlayers) Bukkit.broadcastMessage(theMessage);
    }

    public static void sendErrorMessage(CommandSender sender, String error) {
        sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.RESET + error);
    }

    public static void notify(CommandSender sender, String message) {
        sendMsgToConsole(message);
        Bukkit.broadcast(message, "afk.seeNotifications");
    }
}




































