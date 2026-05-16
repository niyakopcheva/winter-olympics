package org.example.data;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public class Result {
    private UUID athleteId;
    private Duration totalTime = Duration.ofMillis(0);
    private boolean isDNF = false;

    public Result(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
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
