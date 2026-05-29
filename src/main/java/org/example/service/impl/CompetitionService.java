package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.Result;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.example.service.ICompetitionService;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CompetitionService<C extends Competition, R extends Result> implements ICompetitionService<C> {
    protected final ResultService resultService;
    protected final Map<UUID, Duration> totalTimes;

    protected CompetitionService(ResultService resultService) {
        this.resultService = resultService;
        this.totalTimes = new HashMap<>();
    }

    protected abstract List<R> getResults(C competition);

    public abstract void inputResults(C competition, AthleteService athleteService, Olympiad olympiad);

    public void calculateRankings(C competition){
        List<R> competitionResults = getResults(competition);
        competitionResults.removeIf(Result::isDNF);

        totalTimes.clear();

        for(R result : competitionResults){
                Duration totalTime = resultService.calculateTotalTime(result, competition);
                totalTimes.put(result.getAthleteId(), totalTime);
        }

        competitionResults.sort(Comparator
                .comparing(Result::isDNF)
                .thenComparing(r -> totalTimes.get(r.getAthleteId()))
        );
    }

    public void printMedalists(C competition, AthleteService athleteService, String compName){
        calculateRankings(competition);

        System.out.println("\n-----" + compName + " MEDALISTS-----");
        String[] podiumEmojis = {"🥇", "🥈", "🥉"};

        List<R> medalists = getResults(competition).stream()
                .limit(3)
                .collect(Collectors.toList());

        if (medalists.isEmpty()) {
            System.out.println("No athletes successfully finished the race. No medals awarded.");
            return;
        }

        for (int i = 0; i < medalists.size(); i++) {
            R result = medalists.get(i);
            Athlete athlete = athleteService.getAthleteById(result.getAthleteId())
                    .orElseThrow(() -> new AthleteDoesNotExist());

            Duration time = totalTimes.get(result.getAthleteId());
            System.out.println(podiumEmojis[i] + ": " + athlete.getName() + " " + athlete.getCountry() + " | Time: " + time);
        }
    }


    public void printFinalRanking(C competition, AthleteService athleteService) {
        int place = 1;
        List<R> results = getResults(competition);

        for(R result : results){
            Optional<Athlete> athleteOpt = athleteService.getAthleteById(result.getAthleteId());
            if(athleteOpt.isEmpty()) throw new AthleteDoesNotExist();

            Athlete athlete = athleteOpt.get();
            Duration time = totalTimes.get(result.getAthleteId());

            System.out.println(place + ". " +
                    athlete.getName() + " - " +
                    athlete.getCountry() + " | " +
                    String.format("%03d:%o3d", time.toSecondsPart(), time.toMillisPart())
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
