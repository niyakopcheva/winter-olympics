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

public abstract class CompetitionService<C extends Competition> implements ICompetitionService<C> {
    public abstract void inputResults(C competition, AthleteService athleteService, Olympiad olympiad);
    public abstract void calculateRankings(C competition);
    public abstract void printMedalists(C competition, AthleteService athleteService);

    protected <T extends Result> void coreCalculateTotalTimes(List<T> results, Function<T, Duration> formula) {
        for(T result : results){
            if(result.isDNF())
                result.setTotalTime(null);
            else
                result.setTotalTime(formula.apply(result));
        }
    }

//    protected <T extends Result> PriorityQueue<T> getFinalRanking(List<T> results){
//        List<T> finished = results.stream()
//                .filter(r -> !r.isDNF())
//                .collect(Collectors.toList());
//        PriorityQueue<T> ranking = new PriorityQueue<>(Comparator
//                .comparing(Result::getTotalTime)
//        );
//        ranking.addAll(finished);
//        return ranking;
//    }

    protected <T extends Result> void printFinalRanking(List<T> results, AthleteService athleteService) {
        int place = 1;
        for(Result result : results){
            Optional<Athlete> athleteOpt = athleteService.getAthleteById(result.getAthleteId());
            if(athleteOpt.isEmpty()) throw new AthleteDoesNotExist();

            Athlete athlete = athleteOpt.get();
            System.out.println(place + ". " +
                    athlete.getName() + " - " +
                    athlete.getCountry() + " | " +
                    (result.isDNF() ? "DNF" : result.getTotalTime().toString())
            );
            place++;
        }
    }

    protected Duration parseEnteredTime(Scanner scanner, String initialInput) {
        String inputStr = initialInput.trim();

        while (true) {
            if (inputStr.equalsIgnoreCase("DNF") || inputStr.isEmpty()) {
                return null;
            }

            try {
                double seconds = Double.parseDouble(inputStr);
                return Duration.ofMillis((long) (seconds * 1000));
            } catch (NumberFormatException e) {
                System.out.print("!!! Invalid format entered !!! Try again: ");
                inputStr = scanner.nextLine().trim();
            }
        }
    }
}
