package org.example.data;

import java.time.Duration;
import java.util.UUID;

public class SlalomResult extends Result {
    private Duration firstRunTime = Duration.ofMillis(0);
    private Duration secondRunTime = Duration.ofMillis(0);

    public SlalomResult(UUID athleteId) {
        super(athleteId);
    }

    public Duration getFirstRunTime() {
        return firstRunTime;
    }

    public void setFirstRunTime(Duration firstRunTime) {
        if (firstRunTime == null) setDNF(true);
        this.firstRunTime = firstRunTime;
    }

    public Duration getSecondRunTime() {
        return secondRunTime;
    }

    public void setSecondRunTime(Duration secondRunTime) {
        if (secondRunTime == null) setDNF(true);
        this.secondRunTime = secondRunTime;
    }

    @Override
    public String toString() {
        return "SlalomResult{" + "firstRunTime=" + firstRunTime + ", secondRunTime=" + secondRunTime + "} " + super.toString();
    }
}
