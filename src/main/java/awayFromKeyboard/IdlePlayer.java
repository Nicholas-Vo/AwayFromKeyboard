package awayFromKeyboard;

import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import awayFromKeyboard.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class IdlePlayer {
    private final Player thePlayer;
    private boolean isIdle;
    private long timeWentIdle ; // This is the time the player went idle
    private int runnableTaskID ; // This is the taskID to allow for cancelling of the BukkitRunnable
    private int isNowIdleAnnounceTaskID ;

    public IdlePlayer(Player thePlayer, long timeWentIdle) {
        this.thePlayer = thePlayer;
        this.timeWentIdle = timeWentIdle;
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - timeWentIdle;
    }

    public boolean isIdle() { return isIdle; }

    public void setIdle() { // Delay setting of idle by a few seconds
        Bukkit.getScheduler().runTaskLater(AwayFromKeyboard.thePlugin, () -> this.isIdle = true, 2 * 20);

        // Announce "player is now AFK" after a delay to avoid filling the chat
       int taskID = Bukkit.getScheduler().runTaskLater(AwayFromKeyboard.thePlugin, ()
                -> AwayFromKeyboard.broadcastNotification(thePlayer, Messages.isNowAfk),
               ConfigHandler.announcementDelay * 20).getTaskId();
        isNowIdleAnnounceTaskID = taskID; // save taskID for later cancellation
    }

    public void setActive() { // this runs within OnPlayerMove!
        this.timeWentIdle = System.currentTimeMillis();

        if (!this.isIdle) return;

        this.isIdle = false;

        AwayFromKeyboard.broadcastNotification(thePlayer, Messages.noLongerAfk);

        // player's active now; cancel "is now afk" runnable
        Bukkit.getScheduler().cancelTask(isNowIdleAnnounceTaskID);
    }

    public void forget() {
        Bukkit.getScheduler().cancelTask(runnableTaskID);
    }

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
