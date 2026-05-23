package org.example.data;

import java.util.ArrayList;
import java.util.List;

public class Olympiad {
    private final int year;
    private List<Competition> competitions;

    public Olympiad(int year) {
        this.year = year;
        this.competitions = new ArrayList<>();
    }

    public int getYear() {
        return year;
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
