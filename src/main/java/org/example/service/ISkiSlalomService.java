package org.example.service;

import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface ISkiSlalomService extends ICompetitionService<SkiSlalomCompetition> {
    boolean inputFirstRun(SkiSlalomCompetition competition, UUID athleteId, Duration time);
    List<SlalomResult> filterToSecondRun(SkiSlalomCompetition skiSlalomCompetition);
    void inputSecondRun(SkiSlalomCompetition competition, UUID athleteId, Duration time);
}
