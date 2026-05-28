package org.example.service;

import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.Result;
import org.example.service.impl.AthleteService;

import java.util.List;

public interface ICompetitionService<C extends Competition> {
    void inputResults(C competition, AthleteService athleteService, Olympiad olympiad);
    void calculateRankings(C competition, List<Result> competitionResults);
    void printMedalists(C competition, List<Result> competitionResults, AthleteService athleteService, String compName);
    void calculateTotalTimes(C competition);
    void printFinalRanking(C competition, AthleteService athleteService);
}
