package awayFromKeyboard;

import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.temporal.ValueRange;
import java.util.concurrent.TimeUnit;

public class Listeners implements Listener {
    private AwayFromKeyboard afk;
    private BukkitScheduler scheduler;

    public Listeners(AwayFromKeyboard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        afk = plugin;
        scheduler = Bukkit.getScheduler();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        IdlePlayer player = afk.getIdlePlayer(e.getPlayer());
        player.setActive();

        // When the player joins, run a timer every second to check if they're idle
        player.setPrimaryRunnableTaskID(scheduler.runTaskTimer(afk, () -> {
            long idleTime = player.getIdleTime();

            if (idleTime > ConfigHandler.timeBeforeMarkedAFK) {
                player.setIdle();
                return;
            }

            if (player.isKickExempt()) { // kick logic below here
                return;
            }

            long time = ConfigHandler.timeBeforeAutoKick - 1000 * 60;

            if (ConfigHandler.ShouldWarnBeforeKick && ValueRange.of(time, time + 1000).isValidValue(idleTime)) {
                afk.sendMessage(e.getPlayer(), Messages.youAreAboutToBeKicked);
            }

            if (idleTime < ConfigHandler.timeBeforeAutoKick) {
                return;
            }

            if (ConfigHandler.autoKickEnabled) {
                player.kickPlayer(Messages.youHaveBeenAutoKicked);

                if (ConfigHandler.announceAutoKick) {
                    afk.broadcast(e.getPlayer(), Messages.autoKickAnnounce);
                }
            }

        }, 0, 20).getTaskId());

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        IdlePlayer ip = afk.getIdlePlayer(p);

        ConfigHandler.getIdleTriggerMessages().forEach(msg -> {
            if (e.getMessage().toLowerCase().startsWith(msg.toLowerCase())) {
                ip.addToTaskList(scheduler.runTaskLater(afk, ip::setIdle, 2 * 20).getTaskId());
            } else {
                scheduler.runTaskAsynchronously(afk, () -> ip.setActive());
            }

        });

    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (ConfigHandler.getIgnoredCommands().contains(e.getMessage())) { return; }

        scheduler.runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        scheduler.runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
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























