package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.enums.Country;
import org.example.data.enums.Sex;
import org.example.data.exceptions.NegativeValueException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AthleteInput {
    private final AthleteService athleteService;

    public AthleteInput(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    public boolean registerNewAthleteFromConsole(Scanner scanner) {
        System.out.println("\nREGISTER NEW ATHLETE");

        System.out.print("Enter athlete full name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("⚠️ Name cannot be empty! Try again: ");
            name = scanner.nextLine().trim();
        }

        Country country = parseEnteredCountry(scanner);
        LocalDate birthDate = parseEnteredDate(scanner);
        Sex sex = parseEnteredSex(scanner);

        Athlete newAthlete = new Athlete(name, country, sex, birthDate);

        boolean success = athleteService.registerAthlete(newAthlete);
        if (success) {
            System.out.println("Athlete successfully registered.");
            return true;
        } else {
            System.out.println("Registration failed. Athlete might already exist.");
            return false;
        }
    }

    //helpers
    public int parseAthletesCount(Scanner scanner){
        System.out.println("Enter number of athletes in the Winter Olympics: ");
        String inputStr = scanner.nextLine().trim();
        while(true){
            try {
                int count = Integer.parseInt(inputStr);
                if(count <= 0)
                    throw new NegativeValueException("Number of participants must be a positive number!");

                return count;
            } catch (NegativeValueException e){
                System.out.println(e.getMessage() + " Try again: ");
                inputStr = scanner.nextLine().trim();
            } catch (NumberFormatException e){
                System.out.println("Value must be an integer! Try again: ");
                inputStr = scanner.nextLine().trim();
            }
        }
    }

    private LocalDate parseEnteredDate(Scanner scanner) {
        System.out.print("Enter birth date (YYYY-DD-MM): ");
        String inputStr = scanner.nextLine().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");

        while (true) {
            try {
                return LocalDate.parse(inputStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.print("!!! Invalid date format !!! Use YYYY-DD-MM. Try again: ");
                inputStr = scanner.nextLine().trim();
            }
        }
    }

    private Sex parseEnteredSex(Scanner scanner) {
        System.out.print("Enter gender (M/F): ");
        String inputStr = scanner.nextLine().trim().toUpperCase();

        while (true) {
            if(inputStr.equals("M")) return Sex.MALE;
            else if(inputStr.equals("F")) return Sex.FEMALE;
            else {
                System.out.print("!!! Invalid entry !!! Type M/F. Try again: ");
                inputStr = scanner.nextLine().trim().toUpperCase();
            }
        }
    }

    private Country parseEnteredCountry(Scanner scanner){
        System.out.print("Enter country name: ");
        String country = scanner.nextLine().trim().toUpperCase();
        while (country.isEmpty()) {
            System.out.print("⚠️ Country name cannot be empty! Try again: ");
            country = scanner.nextLine().trim().toUpperCase();
        }

        while(true){
            try {
                return Country.valueOf(country);
            } catch (IllegalArgumentException e){
                System.out.print("!!! Invalid entry !!! Country may not be part of the Olympics. Type country name: ");
                country = scanner.nextLine().trim().toUpperCase();
            }
        }
    }
}
