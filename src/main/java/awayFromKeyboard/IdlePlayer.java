package awayFromKeyboard;

import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class IdlePlayer {
    private final Player thePlayer;
    private boolean isIdle;
    private long timeWentIdle; // This is the time the player went idle
    private int runnableTaskID; // This is the taskID to allow for cancelling of the BukkitRunnable
    private String savedTabList;

    public IdlePlayer(Player thePlayer, long timeWentIdle) {
        this.thePlayer = thePlayer;
        this.timeWentIdle = timeWentIdle;
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - timeWentIdle;
    }

    public boolean isIdle() { return isIdle; }

    public void setIdle() {
        if (isIdle) return;
        isIdle = true;

        if (ConfigHandler.shouldDisplayTabListTag) {
            savedTabList = thePlayer.getPlayerListName();
            thePlayer.setPlayerListName(Chat.formatUsername(thePlayer, Messages.tabListTag));
        }

        if (ConfigHandler.announcePlayerNowAfk) { AwayFromKeyboard.broadcast(thePlayer, Messages.isNowAfk); }
    }

    public void setActive() { // this runs within OnPlayerMove!
        timeWentIdle = System.currentTimeMillis();

        if (!isIdle) return;
        isIdle = false;

        if (ConfigHandler.shouldDisplayTabListTag) thePlayer.setPlayerListName(savedTabList);

        if (ConfigHandler.announcePlayerNoLongerAfk) {
            Bukkit.getScheduler().runTaskLater(AwayFromKeyboard.thePlugin, () -> {
                if (thePlayer.isOnline()) { AwayFromKeyboard.broadcast(thePlayer, Messages.noLongerAfk); }
            }, 2 * 20);
        }
    }

    public boolean isKickExempt() { return thePlayer.hasPermission("afk.kickexempt"); }

    public void forget() { Bukkit.getScheduler().cancelTask(runnableTaskID); }

    public void setRunnableTaskID(int runnableTaskID) {
        this.runnableTaskID = runnableTaskID;
    }

    public boolean isOnline() {
        return thePlayer.isOnline();
    }

    public String getName() {
        return thePlayer.getName();
    }

    public boolean hasPermission(String permission) { return thePlayer.hasPermission(permission); }

    public void kickPlayer(String theReason) { thePlayer.kickPlayer(theReason); }

    public Player getPlayer() { return thePlayer; }

    public String timeIdleToString() {
        long seconds = (System.currentTimeMillis() - timeWentIdle) / 1000;

        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);

        if (seconds <= 60) return String.format("% 2ds", seconds);
        if (minutes <= 60) return String.format("% 2dm% 2ds", minutes, seconds % 60);

        else if (hours >= 1) return String.format("% 2dh% 2dm", hours, minutes % 60);
        return String.format("% 2dm", minutes);
    }
}
