package org.example.service;

import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.service.impl.AthleteService;

import java.time.Duration;
import java.util.UUID;

public interface IBiathlonService {
    boolean inputRuntime(BiathlonCompetition competition, UUID athleteId, Duration time);
    boolean inputMissedShots(BiathlonCompetition competition, UUID athleteId, int missedShots);
    Duration calculatePenalty(BiathlonCompetition competition, BiathlonResult result);
    void calculateTotalTimes(BiathlonCompetition competition);
    void printFinalRanking(BiathlonCompetition competition, AthleteService athleteService);
}
