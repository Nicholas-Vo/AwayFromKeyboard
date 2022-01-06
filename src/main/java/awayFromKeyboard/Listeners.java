package awayFromKeyboard;

import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Test;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listeners implements Listener {
    private AwayFromKeyboard afk;

    public Listeners(AwayFromKeyboard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.afk = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        IdlePlayer player = afk.getIdlePlayer(e.getPlayer());

        // When the player joins, run a timer every second to check if they're idle
        int taskID = Bukkit.getScheduler().runTaskTimer(afk, () -> {

            if (player.getIdleTime() > ConfigHandler.timeBeforeMarkedAFK * 1000 * 60) {
                if (!player.isIdle()) {
                    player.setIdle();
                }
            }

            if (!player.isOnline()) { // Player isn't online, so stop monitoring them
                player.forget();
                Test.aTest("Forgetting about " + Chat.red + player.getName());
            }

        }, 20, 120).getTaskId(); // 1-second delay, 1-second period

        player.setRunnableTaskID(taskID); // Save this id to allow for later cancellation
    }

    @EventHandler
    public void onPlayerMoveAFK(PlayerMoveEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerChatAFK(AsyncPlayerChatEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerCommandAFK(PlayerCommandPreprocessEvent e) { // TODO add exception commands
        if (ConfigHandler.getIgnoredCommands().contains(e.getMessage())) { return; }
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }
}























