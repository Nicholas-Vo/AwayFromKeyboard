package awayFromKeyboard;

import awayFromKeyboard.utils.Chat;
import awayFromKeyboard.utils.ConfigHandler;
import awayFromKeyboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class IdlePlayer {
    private final Player thePlayer;
    private boolean isIdle;
    private boolean notifsBlocked;
    private long timeWentIdle; // This is the time the player went idle
    private int runnableTaskID; // This is the taskID to allow for cancelling of the BukkitRunnable
    private String savedTabList;
    private Set<Integer> tasks;

    private BukkitScheduler scheduler;

    public IdlePlayer(Player thePlayer, long timeWentIdle) {
        this.thePlayer = thePlayer;
        this.timeWentIdle = timeWentIdle;
        tasks = new HashSet<>();
        scheduler = Bukkit.getScheduler();
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - timeWentIdle;
    }

    public boolean isIdle() { return isIdle; }

    public void setIdle() {
        if (isIdle) return;
        isIdle = true;

        clearNotificationTasks();

        if (ConfigHandler.shouldDisplayTabListTag) {
            savedTabList = thePlayer.getPlayerListName();
            thePlayer.setPlayerListName(Chat.formatUsername(thePlayer, Messages.tabListTag));
        }

        if (ConfigHandler.announcePlayerNowAfk) {
            if (!notifsBlocked) AwayFromKeyboard.broadcast(thePlayer, Messages.isNowAfk);
            notifsBlocked = true;
            scheduler.runTaskLater(AwayFromKeyboard.thePlugin, () -> notifsBlocked = false, ConfigHandler.afkCommandBufferTime);
        }
    }

    public void setActive() { // this runs within OnPlayerMove!
        timeWentIdle = System.currentTimeMillis();

        if (!isIdle) return;
        isIdle = false;

        if (ConfigHandler.shouldDisplayTabListTag) thePlayer.setPlayerListName(savedTabList);

        if (ConfigHandler.announcePlayerNoLongerAfk) {
            tasks.add(scheduler.runTaskLater(AwayFromKeyboard.thePlugin, () -> {

                if (thePlayer.isOnline()) { AwayFromKeyboard.broadcast(thePlayer, Messages.noLongerAfk); }

            }, 2 * 20).getTaskId()); // todo remove delay?)

            clearNotificationTasks();
        }
    }

    public void forget() {
        scheduler.cancelTask(runnableTaskID);
        clearNotificationTasks();
        AwayFromKeyboard.removeFromIdlePlayerMap(thePlayer.getUniqueId());
    }

    public void clearNotificationTasks() {
        tasks.forEach(id -> scheduler.cancelTask(id));
    }

    public boolean isKickExempt() { return thePlayer.hasPermission("afk.kickexempt"); }

    public void addToTaskList(int taskId) { tasks.add(taskId); }

    public void setPrimaryRunnableTaskID(int runnableTaskID) {
        this.runnableTaskID = runnableTaskID;
    }

    public String getName() {
        return thePlayer.getName();
    }

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
