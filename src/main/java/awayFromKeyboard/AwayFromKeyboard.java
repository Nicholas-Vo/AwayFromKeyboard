package awayFromKeyboard;

import java.io.ObjectInputFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
	public ConcurrentMap<UUID, Boolean> afkMap = new ConcurrentHashMap<UUID, Boolean>();
	public ConcurrentMap<UUID, Long> timeWentAFK = new ConcurrentHashMap<UUID, Long>();
	public ConcurrentMap<UUID, Integer> runnableMap = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, Boolean> inBufferPeriod = new ConcurrentHashMap<UUID, Boolean>();
	public String pluginTag = ChatColor.RED + "[AFK] " + ChatColor.RESET;
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

		Notifier.sendMsgToConsole("Enabling AwayFromKeyboard " + VERSION + "...");
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this); // cancel all tasks
		Notifier.sendMsgToConsole("Disabled AwayFromKeyboard " + VERSION + ".");
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

			if (!player.hasPermission("afk.seeNotifications")) {
				sender.sendMessage(Messages.markedYourselfAfk);
			}

			setAFK(player);
			applyBuffer(player); // prevent removal of afk status for 3 seconds
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

	public void setAFK(Player player) {
		afkMap.put(player.getUniqueId(), true);
		Notifier.notify(player, "isNowAfk");
	}

//	public void removeAFK(Player player) {
//		afkMap.put(player.getUniqueId(), false);
//		if (player.isOnline()) player.sendMessage(Messages.noLongerAfk);
//
//		if (ConfigHandler.displayTabListTag) player.setPlayerListName(player.getName());
//	}

	public void error(CommandSender sender, String error) {
		sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + error);
	}

	public void applyBuffer(Player player) {
		inBufferPeriod.put(player.getUniqueId(), true);
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				inBufferPeriod.put(player.getUniqueId(), false);
			}

		}, 60);
	}

	public static List<SubCommand> getCommandList() {
		return commands;
	}

	public List<IdlePlayer> getAfkPlayers() {
		List<IdlePlayer> afkList = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (playerIsAFK(player)) {
				afkList.add((IdlePlayer) player.getPlayer());
			}
		}
		return afkList;
	}

	public boolean playerIsAFK(Player p) {
		if (afkMap.get(p.getUniqueId()) == null) {
			return false;
		} else {
			return afkMap.get(p.getUniqueId());
		}
	}
}
