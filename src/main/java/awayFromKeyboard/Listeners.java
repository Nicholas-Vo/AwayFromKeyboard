package awayFromKeyboard;

import awayFromKeyboard.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.temporal.ValueRange;

public class Listeners implements Listener {
    private AwayFromKeyboard afk;
    private final ConfigHandler config;

    public Listeners(AwayFromKeyboard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        config = plugin.config();

        this.afk = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        IdlePlayer player = afk.getIdlePlayer(e.getPlayer());
        player.setActive();

        // When the player joins, run a timer every second to check if they're idle
        player.setPrimaryRunnableTaskID(Bukkit.getScheduler().runTaskTimer(afk, () -> {
            long idleTime = player.getIdleTime();
            if (idleTime > config.timeBeforeMarkedAFK) {
                player.setIdle();
                return;
            }

            if (player.isKickExempt()) { // kick logic below here
                return;
            }

            long time = config.timeBeforeAutoKick - 1000 * 60;

            if (config.ShouldWarnBeforeKick && ValueRange.of(time, time + 1000).isValidValue(idleTime)) {
                afk.sendMessage(e.getPlayer(), config.get("youAreAboutToBeKicked"));
            }

            if (idleTime < config.timeBeforeAutoKick) {
                return;
            }

            if (config.autoKickEnabled) {
                player.kickPlayer(config.get("youHaveBeenAutoKicked"));

                if (config.announceAutoKick) {
                    afk.broadcast(e.getPlayer(), config.get(""));
                }
            }
        }, 0, 20).getTaskId());

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        IdlePlayer ip = afk.getIdlePlayer(p);

        config.getIdleTriggerMessages().forEach(msg -> {
            if (e.getMessage().toLowerCase().startsWith(msg.toLowerCase())) {
                ip.addToTaskList(Bukkit.getScheduler().runTaskLater(afk, ip::setIdle, 2 * 20).getTaskId());
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(afk, ip::setActive);
            }
        });
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (config.getIgnoredCommands().contains(e.getMessage())) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        afk.getIdlePlayer(e.getPlayer()).forget();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        afk.getIdlePlayer(e.getPlayer()).forget();
    }

}























