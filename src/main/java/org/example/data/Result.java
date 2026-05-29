package org.example.data;
import java.io.Serializable;
import java.util.UUID;

public abstract class Result implements Serializable {
    private final UUID athleteId;
    private boolean isDNF = false;

    public Result(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public boolean isDNF() {
        return isDNF;
    }

    public void setDNF(boolean DNF) {
        isDNF = DNF;
    }

    @Override
    public String toString() {
        return "Result{" + "athleteId=" + athleteId + ", isDNF=" + isDNF + '}';
    }
}


