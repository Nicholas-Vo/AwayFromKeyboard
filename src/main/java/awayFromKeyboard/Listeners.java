package awayFromKeyboard;

import org.bukkit.Bukkit;
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
        IdlePlayer player = (IdlePlayer) e.getPlayer();

        // When the player joins, run a timer every second to check if they're idle
        int taskID = Bukkit.getScheduler().runTaskTimer(afk, () -> {

            if (player.getIdleTime() > ConfigHandler.timeBeforeMarkedAFK * 1000 * 60) {
                if (!player.isIdle()) {
                    player.setIdle();
                }
            }

            if (!player.isOnline()) { // Player left, so stop monitoring them
                Bukkit.getScheduler().cancelTask(player.getRunnableTaskID());
                player.setActive();
            }
        }, 20, 120).getTaskId(); // 1-second delay, 1-second period

        player.setRunnableTaskID(taskID); // Save this id to allow for later cancellation
    }

    @EventHandler
    public void onPlayerMoveAFK(PlayerMoveEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> ((IdlePlayer) e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerChatAFK(AsyncPlayerChatEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> ((IdlePlayer) e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerCommandAFK(PlayerCommandPreprocessEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> ((IdlePlayer) e.getPlayer()).setActive());
    }
}