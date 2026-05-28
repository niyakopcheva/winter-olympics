package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.data.Olympiad;
import org.example.service.IBiathlonService;

import java.time.Duration;
import java.util.*;

public class BiathlonService extends CompetitionService<BiathlonCompetition> implements IBiathlonService {

    @Override
    public boolean inputRuntime(BiathlonCompetition competition, UUID athleteId, Duration time) {
        Optional<BiathlonResult> resultOpt = competition.getResults().stream()
                .filter(r -> r.getAthleteId().equals(athleteId))
                .findFirst();
        BiathlonResult result;
        if(resultOpt.isPresent()){
            result = resultOpt.get();
            result.setRunTime(time);
            return true;
        }
        else {
            result = new BiathlonResult(athleteId);
            result.setRunTime(time);
            return competition.getResults().add(result);
        }
    }

    @Override
    public boolean inputMissedShots(BiathlonCompetition competition, UUID athleteId, Integer missedShots) {
        Optional<BiathlonResult> resultOpt = competition.getResults().stream()
                .filter(r -> r.getAthleteId().equals(athleteId))
                .findFirst();
        BiathlonResult result;
        if(resultOpt.isPresent()){
            result = resultOpt.get();
            result.setMissedShots(missedShots);
            return true;
        }
        else {
            result = new BiathlonResult(athleteId);
            result.setMissedShots(missedShots);
            return competition.getResults().add(result);
        }
    }

    @Override
    public Duration calculatePenalty(BiathlonCompetition competition, BiathlonResult result) {
        if(result.isDNF())
            return null;
        return competition.getTimePenalty().multipliedBy(result.getMissedShots());
    }

    @Override
    public void calculateTotalTimes(BiathlonCompetition competition) {
        coreCalculateTotalTimes(competition.getResults(),
                r -> calculatePenalty(competition, r).plus(r.getRunTime()));
    }

    @Override
    public void printFinalRanking(BiathlonCompetition competition, AthleteService athleteService) {
        super.printFinalRanking(competition.getResults(), athleteService);
    }

    @Override
    public void inputResults(BiathlonCompetition competition, AthleteService athleteService, Olympiad olympiad) {
        Scanner input = new Scanner(System.in);

        System.out.println("\n-----RUNTIME-----");
        for(Athlete athlete : athleteService.getAllAthletes()){
            if(!athleteService.isEligibleForCompetition(athlete.getId(), competition, olympiad))
                continue;

            System.out.println("Enter run time for " + athlete.getName() + " in seconds (or type 'DNF)': ");

            String rawInput = input.nextLine();
            Duration time = parseEnteredTime(input, rawInput);
            inputRuntime(competition, athlete.getId(), time);
        }

        System.out.println("\n-----MISSED SHOTS-----");
        for(BiathlonResult result : competition.getResults()){
            Athlete athlete = athleteService.getAthleteById(result.getAthleteId()).get();
            System.out.println("Enter missed shots for " + athlete.getName() + " (or type 'DNF)': ");

            String rawInput = input.nextLine();
            Integer missedShots = parseEnteredInt(input, rawInput);
            inputMissedShots(competition, athlete.getId(), missedShots);
        }
    }

    private Integer parseEnteredInt(Scanner scanner, String initialInput){
        String inputStr = initialInput.trim();

        while (true) {
            if (inputStr.equalsIgnoreCase("DNF") || inputStr.isEmpty()) {
                return null;
            }

            try {
                return Integer.parseInt(inputStr);
            } catch (NumberFormatException e) {
                System.out.print("!!! Invalid format entered !!! Try again: ");
                inputStr = scanner.nextLine().trim();
            }
        }
    }
}
