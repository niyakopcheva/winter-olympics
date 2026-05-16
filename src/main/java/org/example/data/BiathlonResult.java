package org.example.data;

import java.time.Duration;
import java.util.UUID;

public class BiathlonResult extends Result {
    private Duration runTime = Duration.ofMillis(0);
    private int missedShots = 0;
    private static Duration timePenalty = Duration.ofMinutes(1);

    public BiathlonResult(UUID athleteId) {
        super(athleteId);
    }

    public Duration getRunTime() {
        return runTime;
    }

    public void setRunTime(Duration runTime) {
        this.runTime = runTime;
    }

    public int getMissedShots() {
        return missedShots;
    }

    public void setMissedShots(int missedShots) {
        this.missedShots = missedShots;
    }

    public static Duration getTimePenalty() {
        return timePenalty;
    }

    public static void setTimePenalty(Duration timePenalty) {
        BiathlonResult.timePenalty = timePenalty;
    }

    @Override
    public String toString() {
        return "BiathlonResult{" + "runTime=" + runTime + ", missedShots=" + missedShots + "} " + super.toString();
    }
}
