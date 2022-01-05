package awayFromKeyboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// todo will this

public abstract class IdlePlayer implements Player {
    private AtomicLong timeWentIdle; // This is the time the player went idle
    private AtomicBoolean isIdle;
    private AtomicInteger runnableTaskID; // This is the taskID to allow for cancelling of the BukkitRunnable

    public String getTimeAFK() {
        long secondsAFK = (System.currentTimeMillis() - timeWentIdle.get()) / 1000;
        String result = String.format("%02dh %02dm", TimeUnit.SECONDS.toHours(secondsAFK),
                TimeUnit.SECONDS.toMinutes(secondsAFK) % TimeUnit.HOURS.toMinutes(1));
        if (secondsAFK < 72000) {
            return String.format("%02d",
                    TimeUnit.SECONDS.toMinutes(secondsAFK) % TimeUnit.HOURS.toMinutes(1)) + "m";
        }
        return result;
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - timeWentIdle.get();
    }

    public boolean isIdle() {
        return isIdle.get();
    }

    public void setIdle() { // Delay setting of idle by 5 seconds
        Bukkit.getScheduler().runTaskLater(AwayFromKeyboard.thePlugin, () -> this.isIdle.set(true), 5 * 20);
    }

    public void setActive() {
        this.timeWentIdle.set(System.currentTimeMillis());
        this.isIdle.set(false);
    }

    public void setRunnableTaskID(int runnableTaskID) {
        this.runnableTaskID.set(runnableTaskID);
    }

    public int getRunnableTaskID() {
        return runnableTaskID.get();
    }

}
