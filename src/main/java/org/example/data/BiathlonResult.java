package org.example.data;

import java.time.Duration;
import java.util.UUID;

public class BiathlonResult extends Result {
    private Duration runTime = Duration.ofMillis(0);
    private int missedShots = -1;

    public BiathlonResult(UUID athleteId) {
        super(athleteId);
    }

    public Duration getRunTime() {
        return runTime;
    }

    public void setRunTime(Duration runTime) throws NegativeValueException {
        if (runTime == null) {
            setDNF(true);
            this.runTime = null;
            return;
        }

        if(runTime.isNegative())
            throw new NegativeValueException("Runtime value cannot be negative!");
        this.runTime = runTime;
    }

    public int getMissedShots() {
        return missedShots;
    }

    public void setMissedShots(Integer missedShots) throws NegativeValueException {
        if(missedShots == null) {
            setDNF(true);
            return;
        }

        if(missedShots<0)
            throw new NegativeValueException("Missed shots value cannot be negative!");

        this.missedShots = missedShots;
    }

    @Override
    public String toString() {
        return "BiathlonResult{" + "runTime=" + runTime + ", missedShots=" + missedShots + "} " + super.toString();
    }
}
