package org.example.data;
import java.io.Serializable;
import java.time.Duration;
import java.util.UUID;

public abstract class Result implements Serializable {
    private final UUID athleteId;
    private Duration totalTime = Duration.ofMillis(0);
    private boolean isDNF = false;

    public Result(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Duration totalTime) {

        this.totalTime = totalTime;
    }

    public boolean isDNF() {
        return isDNF;
    }

    public void setDNF(boolean DNF) {
        isDNF = DNF;
    }

    @Override
    public String toString() {
        return "Result{" + "athleteId=" + athleteId + ", totalTime=" + totalTime + ", isDNF=" + isDNF + '}';
    }
}
