package org.example;

import org.example.data.Athlete;
import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Olympiad;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.enums.Country;
import org.example.data.enums.Sex;
import org.example.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FullLifecycleIntegrationTest {
    private AthleteService athleteService;
    private BiathlonService biathlonService;
    private SkiSlalomService slalomService;
    private OlympiadService olympiadService;
    private Olympiad olympiad;

    @TempDir
    Path tempDir;

    @BeforeEach
    void init(){
        ResultService resultService = new ResultService();
        athleteService = new AthleteService();
        slalomService = new SkiSlalomService(resultService); // Needed for constructor
        biathlonService = new BiathlonService(resultService);
        olympiadService = new OlympiadService(slalomService, biathlonService, athleteService);

        olympiad = new Olympiad(LocalDate.of(2026, 2, 6));
    }

    @Test
    void testBiathlonCompetition() throws IOException {
        //Arrange
        BiathlonCompetition biathlonComp = new BiathlonCompetition(18, Sex.FEMALE);
        olympiad.getCompetitions().add(biathlonComp);
        Path outputFile = tempDir.resolve("final_standings.txt");

        Athlete athlete1 = new Athlete("Athlete 1", Country.NORWAY, Sex.FEMALE, LocalDate.of(1993, 5, 16));
        Athlete athlete2 = new Athlete("Athlete 2", Country.FRANCE, Sex.FEMALE, LocalDate.of(1992, 8, 16));
        Athlete athlete3 = new Athlete("Athlete 3", Country.SWEDEN, Sex.FEMALE, LocalDate.of(1997, 3, 28));
        athleteService.registerAthlete(athlete1);
        athleteService.registerAthlete(athlete2);
        athleteService.registerAthlete(athlete3);

        //Act
        biathlonService.inputRuntime(biathlonComp, athlete1.getId(), Duration.ofMinutes(21));
        biathlonService.inputMissedShots(biathlonComp, athlete1.getId(), 1); // Total - 22min

        biathlonService.inputRuntime(biathlonComp, athlete2.getId(), Duration.ofMinutes(23));
        biathlonService.inputMissedShots(biathlonComp, athlete2.getId(), 2); // Total - 25min

        biathlonService.inputRuntime(biathlonComp, athlete3.getId(), Duration.ofMinutes(21));
        biathlonService.inputMissedShots(biathlonComp, athlete3.getId(), 3); // Total - 24min

        olympiadService.exportStandingsToFile(olympiad, outputFile.toString());

        //Assert
        Map<Country, Integer> medalCounts = olympiadService.getMedalCountByCountry(olympiad);
        assertEquals(1, medalCounts.get(Country.NORWAY));
        assertEquals(1, medalCounts.get(Country.SWEDEN));
        assertEquals(1, medalCounts.get(Country.FRANCE));
        assertEquals(3, medalCounts.size());

        assertTrue(Files.exists(outputFile), "The standings file should have been created.");
        String fileContent = Files.readString(outputFile);

        assertTrue(fileContent.contains("1. Athlete 1 - NORWAY | 22:00:000"), "Winner should be listed first.");
        assertTrue(fileContent.contains("2. Athlete 3 - SWEDEN | 24:00:000"), "Runner-up should be listed second.");
        assertTrue(fileContent.contains("3. Athlete 2 - FRANCE | 25:00:000"), "Third place should be listed third.");
    }

    @Test
    void testSkiSlalomCompetition() throws IOException {
        //Arrange
        SkiSlalomCompetition slalomCompetition = new SkiSlalomCompetition(20, Sex.MALE);
        olympiad.getCompetitions().add(slalomCompetition);
        Path outputFile = tempDir.resolve("final_standings.txt");

        Athlete athlete1 = new Athlete("Athlete 1", Country.UNITED_STATES, Sex.MALE, LocalDate.of(1993, 5, 16));
        Athlete athlete2 = new Athlete("Athlete 2", Country.BULGARIA, Sex.MALE, LocalDate.of(1992, 8, 16));
        Athlete athlete3 = new Athlete("Athlete 3", Country.FINLAND, Sex.MALE, LocalDate.of(1997, 3, 28));
        athleteService.registerAthlete(athlete1);
        athleteService.registerAthlete(athlete2);
        athleteService.registerAthlete(athlete3);

        //Act
        slalomService.inputFirstRun(slalomCompetition, athlete1.getId(), Duration.ofMinutes(20));
        slalomService.inputSecondRun(slalomCompetition, athlete1.getId(), Duration.ofSeconds(315)); // Total - 25:15min

        slalomService.inputFirstRun(slalomCompetition, athlete2.getId(), Duration.ofMinutes(23));
        slalomService.inputSecondRun(slalomCompetition, athlete2.getId(), Duration.ofSeconds(122)); // Total - 25:02min

        slalomService.inputFirstRun(slalomCompetition, athlete3.getId(), Duration.ofMinutes(21));
        slalomService.inputSecondRun(slalomCompetition, athlete3.getId(), null); // DNF

        olympiadService.exportStandingsToFile(olympiad, outputFile.toString());

        //Assert
        Map<Country, Integer> medalCounts = olympiadService.getMedalCountByCountry(olympiad);
        assertEquals(1, medalCounts.get(Country.UNITED_STATES));
        assertEquals(1, medalCounts.get(Country.BULGARIA));
        assertEquals(2, medalCounts.size());

        assertTrue(Files.exists(outputFile), "The standings file should have been created.");
        String fileContent = Files.readString(outputFile);

        assertTrue(fileContent.contains("1. Athlete 2 - BULGARIA | 25:02:000"), "Winner should be listed first.");
        assertTrue(fileContent.contains("2. Athlete 1 - UNITED_STATES | 25:15:000"), "Runner-up should be listed second.");
        assertFalse(fileContent.contains("Athlete 3"), "DNF athlete should not be listed");
    }
}
