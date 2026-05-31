package org.example.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BiathlonServiceTest {

    private BiathlonService biathlonService;
    private ResultService mockResultService;

    @BeforeEach
    void init() {
        mockResultService = mock(ResultService.class);
        biathlonService = new BiathlonService(mockResultService);
    }

    @Test
    void parseEnteredInt_ReturnsInteger_ForValidInput() {
        Scanner mockScanner = mock(Scanner.class);
        String input = "5";

        Integer result = biathlonService.parseEnteredInt(mockScanner, input);

        assertEquals(5, result);
    }

    @Test
    void parseEnteredInt_ReturnsNull_ForDNF() {
        Scanner mockScanner = mock(Scanner.class);
        String input = "DNF";

        Integer result = biathlonService.parseEnteredInt(mockScanner, input);

        assertNull(result);
    }

    @Test
    void parseEnteredInt_ReturnsNull_ForEmptyInput() {
        Scanner mockScanner = mock(Scanner.class);
        String input = "";

        Integer result = biathlonService.parseEnteredInt(mockScanner, input);

        assertNull(result);
    }

    @Test
    void parseEnteredInt_RetriesOnInvalidFormat() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("10");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Integer result = biathlonService.parseEnteredInt(mockScanner, "abc");

            assertEquals(10, result);
            assertTrue(outContent.toString().contains("!!! Invalid format entered !!!"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void parseEnteredInt_RetriesOnNegativeValue() {
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("3");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Integer result = biathlonService.parseEnteredInt(mockScanner, "-1");

            assertEquals(3, result);
            assertTrue(outContent.toString().contains("!!! Missed shots must NOT be negative !!!"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
