package org.example.service;

import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.service.impl.AthleteService;

public interface ICompetitionService<C extends Competition> {
    void inputResults(C competition, AthleteService athleteService, Olympiad olympiad);
    void calculateRankings(C competition);
    void printMedalists(C competition);
    void calculateTotalTimes(C competition);
    void printFinalRanking(C competition, AthleteService athleteService);
}
