package org.example.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Olympiad {
    private final LocalDate startDate;
    private List<Competition> competitions;

    public Olympiad(LocalDate startDate) {
        this.startDate = startDate;
        this.competitions = new ArrayList<>();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) throws IllegalArgumentException{
        if(competitions == null)
            throw new IllegalArgumentException("Competitions cannot be null!");
        this.competitions = competitions;
    }
}
