package org.example.service.UI;

import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.enums.Sex;

import java.util.Scanner;

public class CompetitionInput {
    public void registerCompetitionFromConsole(Scanner scanner, Olympiad olympiad){
        System.out.println("\nCREATE NEW COMPETITION");
        System.out.println("1. Ski Slalom Competition");
        System.out.println("2. Biathlon Competition");
        System.out.print("Select sport type (1 or 2): ");

        String choice = scanner.nextLine().trim();
        while (!choice.equals("1") && !choice.equals("2")) {
            System.out.print("Invalid choice. Please enter 1 or 2: ");
            choice = scanner.nextLine().trim();
        }

        String sportLabel = choice.equals("1") ? "SKI SLALOM" : "BIATHLON";

        Class<? extends Competition> sportClass = choice.equals("1")
                ? SkiSlalomCompetition.class
                : BiathlonCompetition.class;

        Sex sex = parseEnteredSex(scanner);
        int minAge = parseEnteredInt(scanner);

        if(existsCompetitionOfTypeAndSex(olympiad, sportClass, sex)){
            System.out.println("!!! A " + sex + " competition for " + sportLabel + " already exists!");
            System.out.println("Each sport is strictly restricted to 1 MALE and 1 FEMALE event.");
            return;
        }

        Competition newCompetition;

        if(choice.equals("1")) {
            newCompetition = new SkiSlalomCompetition(minAge, sex);
        } else {
            newCompetition = new BiathlonCompetition(minAge, sex);
        }

        olympiad.getCompetitions().add(newCompetition);
        System.out.println(sex + " " + sportLabel + " event officially registered!");
    }

    //helpers
    private Sex parseEnteredSex(Scanner scanner) {
        System.out.print("Enter target athlete gender restriction (M/F): ");
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

    private int parseEnteredInt(Scanner scanner) {
        System.out.print("Enter minimum age requirement rule: ");
        String inputStr = scanner.nextLine().trim();
        while (true) {
            try {
                int age = Integer.parseInt(inputStr);
                if (age < 0) throw new NumberFormatException();
                return age;
            } catch (NumberFormatException e) {
                System.out.print("!!! Invalid number !!! Enter a positive integer for minimum age: ");
                inputStr = scanner.nextLine().trim();
            }
        }
    }

    private boolean existsCompetitionOfTypeAndSex(Olympiad olympiad, Class<? extends Competition> sportClass, Sex sex) {
        if (olympiad.getCompetitions().isEmpty()) {
            return false;
        }

        for (Competition existingComp : olympiad.getCompetitions()) {
            if (existingComp.getClass().equals(sportClass) && existingComp.getSex() == sex) {
                return true;
            }
        }
        return false;
    }
}
