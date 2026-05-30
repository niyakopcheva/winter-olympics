package org.example;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.service.UI.AthleteInput;
import org.example.service.UI.CompetitionInput;
import org.example.service.impl.AthleteService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Olympiad olympiad = new Olympiad(LocalDate.parse("2026-06-02", DateTimeFormatter.ofPattern("yyyy-dd-MM")));

        // INPUT PARTICIPANTS
        AthleteService athleteService = new AthleteService();
        AthleteInput athleteInputUI = new AthleteInput(athleteService);
        int athletesCount = athleteInputUI.parseAthletesCount(input);

        while(athletesCount > 0){
            boolean registerSuccess = athleteInputUI.registerNewAthleteFromConsole(input);
            if(registerSuccess) athletesCount--;
        }

        System.out.println();
        for(Athlete athlete : athleteService.getAllAthletes())
            System.out.println(athlete);
        System.out.println();


        // INPUT COMPETITIONS
        boolean registeringComps = true;
        CompetitionInput competitionInputUI = new CompetitionInput();
        String choice;
        while(registeringComps) {
            System.out.println("Do you want to register a competition? (y/n)");
            choice = input.nextLine().trim().toLowerCase();
            switch (choice){
                case "y":
                    competitionInputUI.registerCompetitionFromConsole(input, olympiad);
                    break;
                case "n":
                    registeringComps = false;
                    break;
                default:
                    System.out.println("!!! Invalid input !!! please write y(Yes) or n(No)");
                    break;
            }
        }

        System.out.println();
        for(Competition competition: olympiad.getCompetitions())
            System.out.println(competition);
        System.out.println();
    }
}