package org.example.service;

import org.example.data.Biathlon.BiathlonCompetition;

import java.time.Duration;
import java.util.UUID;

public interface IBiathlonService extends ICompetitionService<BiathlonCompetition> {
    boolean inputRuntime(BiathlonCompetition competition, UUID athleteId, Duration time);
    boolean inputMissedShots(BiathlonCompetition competition, UUID athleteId, Integer missedShots);
}
