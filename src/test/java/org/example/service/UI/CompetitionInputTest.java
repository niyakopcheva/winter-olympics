package org.example.service.UI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompetitionInputTest {

    private CompetitionInput competitionInput;

    @BeforeEach
    void setUp() {
        competitionInput = new CompetitionInput();
    }

    @Test
    void parseEnteredInt_ReturnsAge_ForValidInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("18");

        int age = competitionInput.parseEnteredInt(mockScanner);

        assertEquals(18, age);
    }

    @Test
    void parseEnteredInt_RetriesOnNonIntegerInput() {
        // Arrange
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("abc", "20");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            int age = competitionInput.parseEnteredInt(mockScanner);

            assertEquals(20, age);
            assertTrue(outContent.toString().contains("!!! Invalid number !!! Enter a positive integer for minimum age: "));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void parseEnteredInt_RetriesOnNegativeInput() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("-5", "16");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            int age = competitionInput.parseEnteredInt(mockScanner);

            assertEquals(16, age);
            assertTrue(outContent.toString().contains("!!! Invalid number !!! Enter a positive integer for minimum age: "));
        } finally {
            System.setOut(originalOut);
        }
    }
}
