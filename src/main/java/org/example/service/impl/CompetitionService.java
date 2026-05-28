package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.Result;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.example.service.ICompetitionService;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CompetitionService<C extends Competition> implements ICompetitionService<C> {
    public abstract void inputResults(C competition, AthleteService athleteService, Olympiad olympiad);
    public abstract void calculateRankings(C competition);
    public abstract void printMedalists(C competition);

    protected <T extends Result> void coreCalculateTotalTimes(List<T> results, Function<T, Duration> formula) {
        for(T result : results){
            if(result.isDNF())
                result.setTotalTime(null);
            else
                result.setTotalTime(formula.apply(result));
        }
    }

    protected <T extends Result> PriorityQueue<T> getFinalRanking(List<T> results){
        List<T> finished = results.stream()
                .filter(r -> !r.isDNF())
                .collect(Collectors.toList());
        PriorityQueue<T> ranking = new PriorityQueue<>(Comparator
                .comparing(Result::getTotalTime)
        );
        ranking.addAll(finished);
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
                    result.getTotalTime()
            );
            place++;
        }
    }
}
