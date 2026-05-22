package org.example.data;

import java.util.ArrayList;
import java.util.List;

public class SkiSlalomCompetition extends Competition {
    private List<SlalomResult> results;

    public SkiSlalomCompetition(int minAge, Sex sex) {
        super(minAge, sex);
        results = new ArrayList<>();
    }

    public List<SlalomResult> getResults() {
        return results;
    }

    public void setResults(List<SlalomResult> results) throws IllegalArgumentException{
        if(results == null)
            throw new IllegalArgumentException("Results cannot be null!");
        this.results = results;
    }

    @Override
    public String toString() {
        return "SkiSlalomCompetition{" + "results=" + results + "} " + super.toString();
    }
}
