package awayFromKeyboard;

import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// todo will this

public class IdlePlayer {
    private final Player thePlayer;
    private final AtomicLong timeWentIdle = new AtomicLong(); // This is the time the player went idle
    private final AtomicBoolean isIdle = new AtomicBoolean();
    private final AtomicInteger runnableTaskID = new AtomicInteger(); // This is the taskID to allow for cancelling of the BukkitRunnable
    private final AtomicInteger isNowIdleAnnounceTaskID = new AtomicInteger();

    public IdlePlayer(Player thePlayer, AtomicLong timeWentIdle) {
        this.thePlayer = thePlayer;
        this.timeWentIdle.set(timeWentIdle.get());
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - timeWentIdle.get();
    }

    public boolean isIdle() { return isIdle.get(); }

    public void setIdle() { // Delay setting of idle by a few seconds
        Bukkit.getScheduler().runTaskLater(AwayFromKeyboard.thePlugin, () -> this.isIdle.set(true), 2 * 20);

        // Announce "player is now AFK" after a delay to avoid filling the chat
       int taskID = Bukkit.getScheduler().runTaskLater(AwayFromKeyboard.thePlugin, () // TODO add delay to config
                -> AwayFromKeyboard.sendFormattedMessage(thePlayer, Messages.isNowAfk), 35 * 20).getTaskId();
        isNowIdleAnnounceTaskID.set(taskID); // save taskID for later cancellation
    }

    public void setActive() {
        this.timeWentIdle.set(System.currentTimeMillis());
        this.isIdle.set(false);

        // player's active now; cancel "is now afk" runnable
        Bukkit.getScheduler().cancelTask(isNowIdleAnnounceTaskID.get());
    }

    public void forget() {
        Bukkit.getScheduler().cancelTask(runnableTaskID.get());
    }

    public void setRunnableTaskID(int runnableTaskID) {
        this.runnableTaskID.set(runnableTaskID);
    }

    public String timeIdleToString() {
        long secondsAFK = (System.currentTimeMillis() - timeWentIdle.get()) / 1000;

        long hours = TimeUnit.SECONDS.toHours(secondsAFK);
        long minutes = TimeUnit.HOURS.toMinutes(hours);

        return secondsAFK < 60 ? String.format("%2ds", secondsAFK) :
                String.format("%02dh %02dm", hours, minutes % TimeUnit.HOURS.toMinutes(1));
    }

    public boolean isOnline() {
        return thePlayer.isOnline();
    }

    public String getName() {
        return thePlayer.getName();
    }

    public void kickPlayer(String theReason) {
        thePlayer.kickPlayer(theReason);
    }

}
