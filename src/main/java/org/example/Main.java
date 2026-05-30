package org.example;

import org.example.data.Athlete;
import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.service.UI.AthleteInput;
import org.example.service.UI.CompetitionInput;
import org.example.service.impl.*;

import java.io.IOException;
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


        // START COMPETITIONS
        ResultService resultService = new ResultService();
        SkiSlalomService slalomService = new SkiSlalomService(resultService);
        BiathlonService biathlonService = new BiathlonService(resultService);
        OlympiadService olympiadService = new OlympiadService(slalomService, biathlonService, athleteService);

        for(Competition competition : olympiad.getCompetitions()){
            if(competition instanceof SkiSlalomCompetition){
                SkiSlalomCompetition slalom = (SkiSlalomCompetition) competition;
                System.out.println("\nSTARTING SKI SLALOM COMPETITION - " + slalom.getSex());
                slalomService.inputResults(slalom, athleteService, olympiad);
//                slalomService.printFinalRanking(slalom, athleteService);
            }
            else if(competition instanceof BiathlonCompetition) {
                BiathlonCompetition biathlon = (BiathlonCompetition) competition;
                System.out.println("\nSTARTING BIATHLON COMPETITION - " + biathlon.getSex());
                biathlonService.inputResults(biathlon, athleteService, olympiad);
//                biathlonService.printFinalRanking(biathlon, athleteService);
            }
        }

        System.out.println("Saving rankings to text file(final_rankings.txt)");
        try{
            olympiadService.exportStandingsToFile(olympiad, "final_rankings.txt");
            System.out.println("Success! Check final_rankings.txt for Olympiad rankings.");
        } catch (IOException e){
            System.out.println("Failed to generate file: " + e.getMessage());
        }
    }
}