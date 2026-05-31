package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.data.enums.Country;
import org.example.data.enums.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OlympiadServiceTest {

    private OlympiadService olympiadService;
    private CompetitionService<SkiSlalomCompetition, SlalomResult> mockSlalomService;
    private CompetitionService<BiathlonCompetition, BiathlonResult> mockBiathlonService;
    private AthleteService mockAthleteService;
    private ResultService mockResultService;

    @TempDir
    Path tempDir;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void init() {
        mockSlalomService = mock(CompetitionService.class);
        mockBiathlonService = mock(CompetitionService.class);
        mockAthleteService = mock(AthleteService.class);
        mockResultService = mock(ResultService.class);

        olympiadService = new OlympiadService(mockSlalomService, mockBiathlonService, mockAthleteService);
    }

    @Test
    void exportStandingsToFile_SuccessfulExport() throws IOException {
        Path filePath = tempDir.resolve("rankings.txt");
        Olympiad olympiad = new Olympiad(LocalDate.of(2026, 1, 1));
        
        BiathlonCompetition competition = new BiathlonCompetition(18, Sex.MALE);
        List<Competition> competitions = new ArrayList<>();
        competitions.add(competition);
        olympiad.setCompetitions(competitions);

        UUID athleteId = UUID.randomUUID();
        BiathlonResult result = new BiathlonResult(athleteId);
        result.setRunTime(Duration.ofMinutes(20));
        result.setMissedShots(2);
        
        Athlete athlete = new Athlete("Ivan Ivanov", Country.BULGARIA, Sex.MALE, LocalDate.of(2000, 1, 1));

        when(mockBiathlonService.getResults(competition)).thenReturn(Collections.singletonList(result));
        when(mockBiathlonService.getResultService()).thenReturn(mockResultService);
        when(mockAthleteService.getAthleteById(athleteId)).thenReturn(Optional.of(athlete));
        when(mockResultService.calculateTotalTime(result, competition)).thenReturn(Duration.ofMinutes(22));

        olympiadService.exportStandingsToFile(olympiad, filePath.toString());

        assertTrue(Files.exists(filePath));
        String content = Files.readString(filePath);
        assertTrue(content.contains("BIATHLON"));
        assertTrue(content.contains("Ivan Ivanov"));
        assertTrue(content.contains("BULGARIA"));
        assertTrue(content.contains("22:00:000"));
    }

    @Test
    void exportStandingsToFile_HandlesIOException() {
        Olympiad olympiad = new Olympiad(LocalDate.of(2026, 1, 1));
        String invalidPath = "/invalid/path/that/does/not/exist/rankings.txt";
        
        assertDoesNotThrow(() -> {
            olympiadService.exportStandingsToFile(olympiad, invalidPath);
        });
    }
}
