package org.example.service;

import org.example.data.Athlete;
import org.example.data.Competition;

import java.util.UUID;

public interface ICompetitionService {
    void inputResults(Competition competition);
    void calculateRankings(Competition competition);
    void printMedalists(Competition competition);
}
