package org.example.data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BiathlonCompetition extends Competition {
    private List<BiathlonResult> results;
    private Duration timePenalty = Duration.ofMinutes(1);

    public BiathlonCompetition(int minAge, Sex sex) {
        super(minAge, sex);
        this.results = new ArrayList<>();
    }

    public List<BiathlonResult> getResults() {
        return results;
    }

    public void setResults(List<BiathlonResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "BiathlonCompetition{" + "results=" + results + "} " + super.toString();
    }

    public Duration getTimePenalty() {
        return timePenalty;
    }

    public void setTimePenalty(Duration timePenalty) throws IllegalArgumentException, NegativeValueException{
        if(timePenalty == null)
            throw new IllegalArgumentException("TimePenalty cannot be null!");
        if(timePenalty.isNegative() || timePenalty.isZero())
            throw new NegativeValueException("TimePenalty must be a positive value!");
        this.timePenalty = timePenalty;
    }
}
