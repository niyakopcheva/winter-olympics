package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Olympiad;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.data.exceptions.AthleteNotQualifiedException;
import org.example.service.ISkiSlalomService;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class SkiSlalomService extends CompetitionService<SkiSlalomCompetition, SlalomResult> implements ISkiSlalomService {
    public SkiSlalomService(ResultService resultService) {
        super(resultService);
    }

    @Override
    protected List<SlalomResult> getResults(SkiSlalomCompetition competition) {
        return competition.getResults();
    }
    @Override
    public boolean inputFirstRun(SkiSlalomCompetition competition, UUID athleteId, Duration time) {
        if(competition.getResults().stream().anyMatch(r -> r.getAthleteId().equals(athleteId)))
            return false;

        SlalomResult slalomResult = new SlalomResult(athleteId);
        slalomResult.setFirstRunTime(time);
        return competition.getResults().add(slalomResult);
    }

    @Override
    public List<SlalomResult> filterToSecondRun(SkiSlalomCompetition skiSlalomCompetition) {
        int size = 30;

        return skiSlalomCompetition.getResults()
                .stream()
                .filter(r -> !r.isDNF())
                .sorted(Comparator.comparing(SlalomResult::getFirstRunTime))
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public void inputSecondRun(SkiSlalomCompetition competition, UUID athleteId, Duration time) throws  AthleteNotQualifiedException{
        List<SlalomResult> qualified = filterToSecondRun(competition);
        Optional<SlalomResult> resultOpt = qualified.stream()
                .filter(r -> r.getAthleteId().equals(athleteId))
                .findFirst();

        if(resultOpt.isEmpty())
            throw new AthleteNotQualifiedException();
        resultOpt.get().setSecondRunTime(time);
    }

    @Override
    public void printFinalRanking(SkiSlalomCompetition slalomCompetition, AthleteService athleteService) {
        calculateRankings(slalomCompetition);
        super.printFinalRanking(slalomCompetition, athleteService);
    }



    @Override
    public void inputResults(SkiSlalomCompetition competition, AthleteService athleteService, Olympiad olympiad) {
        Scanner input = new Scanner(System.in);

        System.out.println("\n-----FIRST RUN-----");
        for(Athlete athlete : athleteService.getAllAthletes()){
            if(!athleteService.isEligibleForCompetition(athlete.getId(), competition, olympiad))
                continue;

            System.out.println("Enter first run time for " + athlete.getName() + " in seconds (or type 'DNF)': ");
            String rawInput = input.nextLine();

            Duration time1 = parseEnteredTime(input, rawInput);
            inputFirstRun(competition, athlete.getId(), time1);
        }

        List<SlalomResult> qualifiedForSecondRun = filterToSecondRun(competition);

        for(SlalomResult result : competition.getResults()){
            if(!qualifiedForSecondRun.contains(result))
                result.setDNF(true);
        }

        System.out.println("\n-----SECOND RUN-----");
        for(SlalomResult result : qualifiedForSecondRun){
            Athlete athlete = athleteService.getAthleteById(result.getAthleteId()).get();
            System.out.println("Enter second run time for " + athlete.getName() + " in seconds (or type 'DNF)': ");
            String rawInput = input.nextLine();

            Duration time2 = parseEnteredTime(input, rawInput);
            inputSecondRun(competition, athlete.getId(), time2);
        }
    }
}
