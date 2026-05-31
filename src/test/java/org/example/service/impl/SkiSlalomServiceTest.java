package org.example.service.impl;

import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.data.exceptions.AthleteNotQualifiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkiSlalomServiceTest {

    private SkiSlalomService skiSlalomService;
    private ResultService mockResultService;

    @BeforeEach
    void setUp() {
        mockResultService = mock(ResultService.class);
        skiSlalomService = new SkiSlalomService(mockResultService);
    }

    @Test
    void inputSecondRun_SetsTime_WhenAthleteIsQualified() {
        SkiSlalomCompetition mockCompetition = mock(SkiSlalomCompetition.class);
        UUID athleteId = UUID.randomUUID();
        SlalomResult mockResult = mock(SlalomResult.class);
        
        when(mockResult.getAthleteId()).thenReturn(athleteId);
        when(mockResult.isDNF()).thenReturn(false);
        when(mockResult.getFirstRunTime()).thenReturn(Duration.ofSeconds(50));
        
        List<SlalomResult> results = new ArrayList<>();
        results.add(mockResult);
        when(mockCompetition.getResults()).thenReturn(results);

        Duration secondRunTime = Duration.ofSeconds(48);
        skiSlalomService.inputSecondRun(mockCompetition, athleteId, secondRunTime);

        verify(mockResult).setSecondRunTime(secondRunTime);
    }

    @Test
    void inputSecondRun_ThrowsException_WhenAthleteIsNotQualified() {
        SkiSlalomCompetition mockCompetition = mock(SkiSlalomCompetition.class);
        UUID athleteId = UUID.randomUUID();

        List<SlalomResult> results = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            SlalomResult betterResult = mock(SlalomResult.class);
            when(betterResult.getAthleteId()).thenReturn(UUID.randomUUID());
            when(betterResult.isDNF()).thenReturn(false);
            when(betterResult.getFirstRunTime()).thenReturn(Duration.ofSeconds(10 + i));
            results.add(betterResult);
        }

        SlalomResult slowResult = mock(SlalomResult.class);
        when(slowResult.getAthleteId()).thenReturn(athleteId);
        when(slowResult.isDNF()).thenReturn(false);
        when(slowResult.getFirstRunTime()).thenReturn(Duration.ofSeconds(100));
        results.add(slowResult);
        
        when(mockCompetition.getResults()).thenReturn(results);

        assertThrows(AthleteNotQualifiedException.class, () -> {
            skiSlalomService.inputSecondRun(mockCompetition, athleteId, Duration.ofSeconds(48));
        });
    }

    @Test
    void inputSecondRun_ThrowsException_WhenAthleteIsDNFInFirstRun() {
        SkiSlalomCompetition mockCompetition = mock(SkiSlalomCompetition.class);
        UUID athleteId = UUID.randomUUID();
        SlalomResult mockResult = mock(SlalomResult.class);
        
        when(mockResult.getAthleteId()).thenReturn(athleteId);
        when(mockResult.isDNF()).thenReturn(true); // Athlete DNF'd
        
        List<SlalomResult> results = new ArrayList<>();
        results.add(mockResult);
        when(mockCompetition.getResults()).thenReturn(results);

        assertThrows(AthleteNotQualifiedException.class, () -> {
            skiSlalomService.inputSecondRun(mockCompetition, athleteId, Duration.ofSeconds(48));
        });
    }
}
