package awayFromKeyboard;

import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.concurrent.TimeUnit;

public class Listeners implements Listener {
    private AwayFromKeyboard afk;

    public Listeners(AwayFromKeyboard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        afk = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        IdlePlayer player = afk.getIdlePlayer(e.getPlayer());
        player.setActive();

        // When the player joins, run a timer every second to check if they're idle
        int taskID = Bukkit.getScheduler().runTaskTimer(afk, () -> {
            long idleTime = player.getIdleTime();

            if (idleTime > ConfigHandler.timeBeforeMarkedAFK) {
                player.setIdle();
                return;
            }

            if (player.isKickExempt() || idleTime < ConfigHandler.timeBeforeAutoKick) {
                return;
            }

            boolean timeToWarn = idleTime == ConfigHandler.timeBeforeAutoKick - TimeUnit.MILLISECONDS.toMinutes(1);
            if (ConfigHandler.ShouldWarnBeforeKick && timeToWarn) {
                afk.sendMessage(e.getPlayer(), Messages.youAreAboutToBeKicked);
            }

            if (ConfigHandler.autoKickEnabled) {
                player.kickPlayer(Messages.youHaveBeenAutoKicked);

                if (ConfigHandler.announceAutoKick) {
                    afk.broadcast(e.getPlayer(), Messages.autoKickAnnounce);
                }
            }

        }, 0, 20).getTaskId(); // todo: 20, 120 - correct?

        player.setRunnableTaskID(taskID); // Save this id to allow for later cancellation
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(afk, () -> afk.getIdlePlayer(e.getPlayer()).setActive());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (ConfigHandler.getIgnoredCommands().contains(e.getMessage())) { return; }
        if (e.getMessage().startsWith("/afk")) { return; } // Setting yourself afk will trigger this setActive()
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























