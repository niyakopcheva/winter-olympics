package org.example;

import org.example.service.impl.AthleteInput;
import org.example.service.impl.AthleteService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // INPUT PARTICIPANTS
        AthleteService athleteService = new AthleteService();
        AthleteInput athleteInputUI = new AthleteInput(athleteService);
        int athletesCount = athleteInputUI.parseAthletesCount(input);

        while(athletesCount > 0){
            boolean registerSuccess = athleteInputUI.registerNewAthleteFromConsole(input);
            if(registerSuccess) athletesCount--;
        }

//        for(Athlete athlete : athleteService.getAllAthletes())
//            System.out.println(athlete);


        // INPUT COMPETITIONS
    }
}