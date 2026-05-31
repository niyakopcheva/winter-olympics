package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.Result;
import org.example.data.enums.Country;
import org.example.data.enums.Sex;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompetitionServiceTest {

    private TestCompetitionService service;
    private ResultService mockResultService;
    private AthleteService mockAthleteService;
    private Competition mockCompetition;

    private static class TestCompetitionService extends CompetitionService<Competition, Result> {
        private List<Result> results = new ArrayList<>();

        protected TestCompetitionService(ResultService resultService) {
            super(resultService);
        }

        @Override
        protected List<Result> getResults(Competition competition) {
            return results;
        }

        @Override
        public void inputResults(Competition competition, AthleteService athleteService, Olympiad olympiad) {
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public Map<UUID, Duration> getTotalTimes() {
            return totalTimes;
        }
    }

    @BeforeEach
    void init() {
        mockResultService = mock(ResultService.class);
        mockAthleteService = mock(AthleteService.class);
        mockCompetition = mock(Competition.class);
        service = new TestCompetitionService(mockResultService);
    }

    @Test
    void printFinalRanking_PrintsCorrectInformation() {
        // Arrange
        UUID athleteId = UUID.randomUUID();
        Result mockResult = mock(Result.class);
        when(mockResult.getAthleteId()).thenReturn(athleteId);
        service.setResults(Collections.singletonList(mockResult));

        Athlete athlete = new Athlete("Alice", Country.CANADA, Sex.FEMALE, LocalDate.of(1995, 5, 5));
        when(mockAthleteService.getAthleteById(athleteId)).thenReturn(Optional.of(athlete));
        
        service.getTotalTimes().put(athleteId, Duration.ofMinutes(1).plusSeconds(20).plusMillis(500));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            service.printFinalRanking(mockCompetition, mockAthleteService);

            String output = outContent.toString();
            assertTrue(output.contains("1. Alice - CANADA | 01:20:500"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void printFinalRanking_ThrowsException_WhenAthleteNotFound() {
        UUID athleteId = UUID.randomUUID();
        Result mockResult = mock(Result.class);
        when(mockResult.getAthleteId()).thenReturn(athleteId);
        service.setResults(Collections.singletonList(mockResult));

        when(mockAthleteService.getAthleteById(athleteId)).thenReturn(Optional.empty());

        assertThrows(AthleteDoesNotExist.class, () -> {
            service.printFinalRanking(mockCompetition, mockAthleteService);
        });
    }

    @Test
    void parseEnteredTime_ReturnsDuration_ForValidInput() {
        Scanner mockScanner = mock(Scanner.class);
        String input = "75.5";

        Duration result = service.parseEnteredTime(mockScanner, input);

        assertEquals(Duration.ofMillis(75500), result);
    }

    @Test
    void parseEnteredTime_ReturnsNull_ForDNF() {
        Scanner mockScanner = mock(Scanner.class);
        Duration result = service.parseEnteredTime(mockScanner, "DNF");
        assertNull(result);
    }

    @Test
    void parseEnteredTime_RetriesOnInvalidFormat() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("40.0"); 
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Duration result = service.parseEnteredTime(mockScanner, "asd");

            assertEquals(Duration.ofSeconds(40), result);
            assertTrue(outContent.toString().contains("!!! Invalid format entered !!!"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void parseEnteredTime_RetriesOnNegativeValue() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("10.0");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Duration result = service.parseEnteredTime(mockScanner, "-5.0");

            assertEquals(Duration.ofSeconds(10), result);
            assertTrue(outContent.toString().contains("!!! Time must be a positive number !!!"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
