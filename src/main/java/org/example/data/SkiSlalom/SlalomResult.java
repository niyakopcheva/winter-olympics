package org.example.data.SkiSlalom;

import org.example.data.Result;
import org.example.data.exceptions.NegativeValueException;

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

    public void setFirstRunTime(Duration firstRunTime) throws NegativeValueException {
        if (firstRunTime == null) {
            setDNF(true);
        }

        if(firstRunTime.isNegative() || firstRunTime.isZero())
            throw new NegativeValueException("FirstRunTime must be a positive value!");

        this.firstRunTime = firstRunTime;
    }

    public Duration getSecondRunTime() {
        return secondRunTime;
    }

    public void setSecondRunTime(Duration secondRunTime) throws NegativeValueException{
        if (secondRunTime == null) {
            setDNF(true);
            return;
        }

        if(secondRunTime.isNegative() || secondRunTime.isZero())
            throw new NegativeValueException("SecondRunTime must be a positive value!");

        this.secondRunTime = secondRunTime;
    }

    @Override
    public String toString() {
        return "SlalomResult{" + "firstRunTime=" + firstRunTime + ", secondRunTime=" + secondRunTime + "} " + super.toString();
    }
}
