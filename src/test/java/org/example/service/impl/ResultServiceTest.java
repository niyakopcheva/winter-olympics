package org.example.service.impl;

import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.data.Competition;
import org.example.data.Result;
import org.example.data.SkiSlalom.SlalomResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultServiceTest {

    private ResultService resultService;

    @BeforeEach
    void init() {
        resultService = new ResultService();
    }

    @Test
    void calculateTotalTime_ReturnsNull_WhenResultIsNull() {
        assertNull(resultService.calculateTotalTime(null, mock(Competition.class)));
    }

    @Test
    void calculateTotalTime_ReturnsNull_WhenResultIsDNF() {
        Result mockResult = mock(Result.class);
        when(mockResult.isDNF()).thenReturn(true);
        
        assertNull(resultService.calculateTotalTime(mockResult, mock(Competition.class)));
    }

    @Test
    void calculateTotalTime_CalculatesSlalomTimeCorrectly() {
        SlalomResult mockSlalomResult = mock(SlalomResult.class);
        when(mockSlalomResult.isDNF()).thenReturn(false);
        when(mockSlalomResult.getFirstRunTime()).thenReturn(Duration.ofSeconds(45));
        when(mockSlalomResult.getSecondRunTime()).thenReturn(Duration.ofSeconds(47));

        Duration totalTime = resultService.calculateTotalTime(mockSlalomResult, mock(Competition.class));

        assertEquals(Duration.ofSeconds(92), totalTime);
    }

    @Test
    void calculateTotalTime_CalculatesBiathlonTimeCorrectly() {
        BiathlonResult mockBiathlonResult = mock(BiathlonResult.class);
        BiathlonCompetition mockBiathlonComp = mock(BiathlonCompetition.class);

        when(mockBiathlonResult.isDNF()).thenReturn(false);
        when(mockBiathlonResult.getRunTime()).thenReturn(Duration.ofMinutes(20));
        when(mockBiathlonResult.getMissedShots()).thenReturn(2);
        
        when(mockBiathlonComp.getTimePenalty()).thenReturn(Duration.ofMinutes(1));

        Duration totalTime = resultService.calculateTotalTime(mockBiathlonResult, mockBiathlonComp);

        assertEquals(Duration.ofMinutes(22), totalTime);
    }

    @Test
    void calculateTotalTime_ThrowsException_ForUnsupportedCombination() {
        BiathlonResult mockBiathlonResult = mock(BiathlonResult.class);
        Competition genericCompetition = mock(Competition.class);

        when(mockBiathlonResult.isDNF()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            resultService.calculateTotalTime(mockBiathlonResult, genericCompetition);
        });
    }
}
