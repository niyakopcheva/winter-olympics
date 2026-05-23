package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.data.exceptions.AthleteNotQualifiedException;
import org.example.service.ISkiSlalomService;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class SkiSlalomService extends CompetitionService implements ISkiSlalomService {

    @Override
    public boolean inputFirstRun(SkiSlalomCompetition competition, UUID athleteId, Duration time) {
        SlalomResult slalomResult = new SlalomResult(athleteId);
        if(competition.getResults().stream().anyMatch(r -> r.getAthleteId().equals(athleteId)))
            return false;

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
    public void inputSecondRun(SkiSlalomCompetition competition, UUID athleteId, Duration time) {
        List<SlalomResult> qualified = filterToSecondRun(competition);
        Optional<SlalomResult> resultOpt = qualified.stream()
                .filter(r -> r.getAthleteId().equals(athleteId))
                .findFirst();

        if(resultOpt.isEmpty())
            throw new AthleteNotQualifiedException();
        resultOpt.get().setSecondRunTime(time);
    }

    @Override
    public void calculateTotalTimes(SkiSlalomCompetition slalomCompetition) {
        for(SlalomResult result : slalomCompetition.getResults()){
            if(result.isDNF())
                result.setTotalTime(null);
            else
                result.setTotalTime(result.getFirstRunTime().plus(result.getSecondRunTime()));
        }
    }

    @Override
    public void printFinalRanking(SkiSlalomCompetition slalomCompetition) {
//        PriorityQueue<SlalomResult> finalResults
    }

//    private PriorityQueue<SlalomResult> getFinalRanking(){
//
//    }
}
