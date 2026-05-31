package org.example.service.UI;

import org.example.service.impl.AthleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AthleteInputTest {

    private AthleteInput athleteInput;

    @BeforeEach
    void setUp() {
        AthleteService mockAthleteService = mock(AthleteService.class);
        athleteInput = new AthleteInput(mockAthleteService);
    }

    @Test
    void parseAthletesCount_ReturnsCount_ForValidInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("10");

        int count = athleteInput.parseAthletesCount(mockScanner);

        assertEquals(10, count);
    }

    @Test
    void parseAthletesCount_RetriesOnNonIntegerInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("abc", "5");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            int count = athleteInput.parseAthletesCount(mockScanner);

            assertEquals(5, count);
            assertTrue(outContent.toString().contains("Value must be an integer! Try again: "));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void parseAthletesCount_RetriesOnZeroInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("0", "3");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            int count = athleteInput.parseAthletesCount(mockScanner);

            assertEquals(3, count);
            assertTrue(outContent.toString().contains("Number of participants must be a positive number!"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void parseAthletesCount_RetriesOnNegativeInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("-1", "1");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            int count = athleteInput.parseAthletesCount(mockScanner);

            assertEquals(1, count);
            assertTrue(outContent.toString().contains("Number of participants must be a positive number!"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
