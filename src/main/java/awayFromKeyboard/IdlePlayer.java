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
    private final Set<Integer> tasks = new HashSet<>();

    private final AwayFromKeyboard afk = AwayFromKeyboard.getInstance();
    private final ConfigHandler config = afk.getConfigHandler();

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

        clearNotificationTasks();

        if (config.shouldDisplayTabListTag) {
            savedTabList = null;
            thePlayer.setPlayerListName(Chat.formatUsername(thePlayer, Messages.TAB_LIST_TAG));
        }

        if (config.announcePlayerNowAfk) {
            if (!notifsBlocked) {
                afk.broadcast(thePlayer, Messages.IS_NOW_AFK);
            }

            notifsBlocked = true;
            Bukkit.getScheduler().runTaskLater(afk,
                    () -> notifsBlocked = false, config.afkCommandBufferTime);
        }
    }

    public void setActive() { // this runs within OnPlayerMove!
        timeWentIdle = System.currentTimeMillis();

        if (!isIdle) {
            return;
        }

        isIdle = false;

        if (config.shouldDisplayTabListTag) {
            thePlayer.setPlayerListName(savedTabList);
        }

        if (config.announcePlayerNoLongerAfk) {
            tasks.add(Bukkit.getScheduler().runTaskLater(afk, () -> {

                if (thePlayer.isOnline()) { afk.broadcast(thePlayer, Messages.NO_LONGER_AFK); }

            }, 2 * 20).getTaskId()); // todo remove delay?)

            clearNotificationTasks();
        }
    }

    public void forget() {
        Bukkit.getScheduler().cancelTask(runnableTaskID);
        clearNotificationTasks();
        afk.removeFromIdlePlayerMap(thePlayer.getUniqueId());
    }

    public void clearNotificationTasks() {
        tasks.forEach(id -> Bukkit.getScheduler().cancelTask(id));
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
