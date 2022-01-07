package awayFromKeyboard;

import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class Listeners implements Listener {
    private AwayFromKeyboard afk;

    public Listeners(AwayFromKeyboard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.afk = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        IdlePlayer player = afk.getIdlePlayer(e.getPlayer());
        player.setActive();

        // When the player joins, run a timer every second to check if they're idle
        int taskID = Bukkit.getScheduler().runTaskTimer(afk, () -> {

            if (player.getIdleTime() > ConfigHandler.timeBeforeMarkedAFK * 1000 * 60) {
                if (!player.isIdle()) {
                    player.setIdle();
                }
            }

            boolean playerEligible = !player.hasPermission("afk.kickexempt") &&
                    player.getIdleTime() >= ConfigHandler.timeBeforeAutoKick * 1000 * 60;

            if (ConfigHandler.shouldWarnPlayersBeforeAutoKick) {
                afk.sendMessage(e.getPlayer(), Messages.youAreAboutToBeKicked);
            }

            if (ConfigHandler.autoKickEnabled && playerEligible) {
                player.kickPlayer(Messages.youHaveBeenAutoKicked);

                if (ConfigHandler.announceAutoKick) {
                    afk.broadcast(e.getPlayer(), Messages.autoKickAnnounce);
                }
            }

        }, 20, 120).getTaskId(); // todo: 20, 120 - correct?

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

    @EventHandler
    public void onPlayerQuitAFK(PlayerQuitEvent e) {
        afk.getIdlePlayer(e.getPlayer()).forget();
    }

}























