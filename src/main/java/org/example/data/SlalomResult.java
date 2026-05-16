package org.example.data;

import java.math.BigDecimal;
import java.util.UUID;

public class SlalomResult extends Result {
    private BigDecimal firstRunTime = BigDecimal.ZERO;
    private BigDecimal secondRunTime = BigDecimal.ZERO;

    public SlalomResult(UUID athleteId) {
        super(athleteId);
    }

    public BigDecimal getFirstRunTime() {
        return firstRunTime;
    }

    public void setFirstRunTime(BigDecimal firstRunTime) {
        if (firstRunTime.compareTo(null) == 0) setDNF(true);
        this.firstRunTime = firstRunTime;
    }

    public BigDecimal getSecondRunTime() {
        return secondRunTime;
    }

    public void setSecondRunTime(BigDecimal secondRunTime) {
        if (secondRunTime.compareTo(null) == 0) setDNF(true);
        this.secondRunTime = secondRunTime;
    }

    @Override
    public String toString() {
        return "SlalomResult{" + "firstRunTime=" + firstRunTime + ", secondRunTime=" + secondRunTime + "} " + super.toString();
    }
}
