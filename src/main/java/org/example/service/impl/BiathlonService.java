package org.example.service.impl;

import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.service.IBiathlonService;

import java.time.Duration;
import java.util.*;

public class BiathlonService extends CompetitionService implements IBiathlonService {

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
    public boolean inputMissedShots(BiathlonCompetition competition, UUID athleteId, int missedShots) {
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
}
