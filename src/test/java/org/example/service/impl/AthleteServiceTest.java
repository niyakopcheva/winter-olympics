package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.enums.Sex;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AthleteServiceTest {

    private AthleteService athleteService;
    private Athlete mockAthlete;
    private Competition mockCompetition;
    private Olympiad mockOlympiad;

    @BeforeEach
    void init() {
        athleteService = new AthleteService();
        mockAthlete = mock(Athlete.class);
        mockCompetition = mock(Competition.class);
        mockOlympiad = mock(Olympiad.class);
    }

    @Test
    void isEligibleForCompetition_ThrowsException_WhenAthleteDoesNotExist() {
        UUID randomId = UUID.randomUUID();

        assertThrows(AthleteDoesNotExist.class, () -> {
            athleteService.isEligibleForCompetition(randomId, mockCompetition, mockOlympiad);
        });
    }

    @Test
    void isEligibleForCompetition_ReturnsTrue_WhenAthleteIsEligible() {
        UUID athleteId = UUID.randomUUID();
        when(mockAthlete.getId()).thenReturn(athleteId);
        when(mockAthlete.getSex()).thenReturn(Sex.MALE);
        when(mockAthlete.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
        
        when(mockCompetition.getSex()).thenReturn(Sex.MALE);
        when(mockCompetition.getMinAge()).thenReturn(18);
        
        when(mockOlympiad.getStartDate()).thenReturn(LocalDate.of(2026, 1, 1));
        
        athleteService.registerAthlete(mockAthlete);
        
        boolean result = athleteService.isEligibleForCompetition(athleteId, mockCompetition, mockOlympiad);
        
        assertTrue(result);
    }

    @Test
    void isEligibleForCompetition_ReturnsFalse_WhenAthleteIsNotEligibleBySex() {
        UUID athleteId = UUID.randomUUID();
        when(mockAthlete.getId()).thenReturn(athleteId);
        when(mockAthlete.getSex()).thenReturn(Sex.FEMALE);
        when(mockAthlete.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
        
        when(mockCompetition.getSex()).thenReturn(Sex.MALE);
        when(mockCompetition.getMinAge()).thenReturn(18);
        
        when(mockOlympiad.getStartDate()).thenReturn(LocalDate.of(2026, 1, 1));
        
        athleteService.registerAthlete(mockAthlete);
        
        boolean result = athleteService.isEligibleForCompetition(athleteId, mockCompetition, mockOlympiad);
        
        assertFalse(result);
    }


}
