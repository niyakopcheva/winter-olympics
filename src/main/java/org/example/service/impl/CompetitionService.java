package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Result;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.example.service.ICompetitionService;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

public class CompetitionService implements ICompetitionService {
    @Override
    public void inputResults(Competition competition) {

    }

    @Override
    public void calculateRankings(Competition competition) {

    }

    @Override
    public void printMedalists(Competition competition) {

    }

    protected <T extends Result> void coreCalculateTotalTimes(List<T> results, Function<T, Duration> formula) {
        for(T result : results){
            if(result.isDNF())
                result.setTotalTime(null);
            else
                result.setTotalTime(formula.apply(result));
        }
    }

    protected <T extends Result> PriorityQueue<T> getFinalRanking(List<T> results){
        PriorityQueue<T> ranking = new PriorityQueue<>(Comparator
                .comparing(Result::isDNF)
                .thenComparing(Result::getTotalTime)
        );
        ranking.addAll(results);
        return ranking;
    }

    protected <T extends Result> void printFinalRanking(List<T> results, AthleteService athleteService) {
        PriorityQueue<T> finalResults = getFinalRanking(results);
        int place = 1;
        while (!finalResults.isEmpty()){
            T result = finalResults.poll();
            Optional<Athlete> athleteOpt = athleteService.getAthleteById(result.getAthleteId());
            if(athleteOpt.isEmpty())
                throw new AthleteDoesNotExist();

            Athlete athlete = athleteOpt.get();
            System.out.println(place + ". " +
                    athlete.getName() + " - " +
                    athlete.getCountry() + " | " +
                    (result.isDNF() ? "DNF" : result.getTotalTime())
            );
            place++;
        }
    }
}
