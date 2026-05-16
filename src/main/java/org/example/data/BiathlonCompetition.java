package org.example.data;

import java.util.ArrayList;
import java.util.List;

public class BiathlonCompetition extends Competition {
    private List<BiathlonResult> results;

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
}
