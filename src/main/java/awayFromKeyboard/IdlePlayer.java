package awayFromKeyboard;

import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public abstract class IdlePlayer implements Player {
    long timeWentIdle;
    boolean isIdle;

    public String getTimeAFK(Player p) {
        long secondsAFK = (System.currentTimeMillis() - timeWentIdle) / 1000;
        String result = String.format("%02dh %02dm", TimeUnit.SECONDS.toHours(secondsAFK),
                TimeUnit.SECONDS.toMinutes(secondsAFK) % TimeUnit.HOURS.toMinutes(1));
        if (secondsAFK < 72000) {
            return String.format("%02d",
                    TimeUnit.SECONDS.toMinutes(secondsAFK) % TimeUnit.HOURS.toMinutes(1)) + "m";
        }
        return result;
    }

    public void setAfk(boolean isIdle) {
        this.isIdle = isIdle;
    }

}
