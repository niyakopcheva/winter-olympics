package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.Result;
import org.example.data.SkiSlalom.SkiSlalomCompetition;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.data.enums.Country;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.example.service.IOlympiadService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class OlympiadService implements IOlympiadService {
    private final CompetitionService<SkiSlalomCompetition, SlalomResult> slalomService;
    private final CompetitionService<BiathlonCompetition, BiathlonResult> biathlonService;
    private final AthleteService athleteService;

    public OlympiadService(CompetitionService<SkiSlalomCompetition, SlalomResult> slalomService, CompetitionService<BiathlonCompetition, BiathlonResult> biathlonService, AthleteService athleteService) {
        this.slalomService = slalomService;
        this.biathlonService = biathlonService;
        this.athleteService = athleteService;
    }

    @Override
    public Map<Country, Integer> getMedalCountByCountry(Olympiad olympiad) {
        Map<Country, Integer> medalsByCountry = new HashMap<>();
        for(Competition competition : olympiad.getCompetitions()){
            List<? extends Result> finalPodium = new ArrayList<>();

            if (competition instanceof SkiSlalomCompetition) {
                SkiSlalomCompetition slalom = (SkiSlalomCompetition) competition;
                slalomService.calculateRankings(slalom);

                finalPodium = slalom.getResults().stream()
                        .limit(3)
                        .collect(Collectors.toList());

            } else if (competition instanceof BiathlonCompetition) {
                BiathlonCompetition biathlon = (BiathlonCompetition) competition;
                biathlonService.calculateRankings(biathlon);

                finalPodium = biathlon.getResults().stream()
                        .limit(3)
                        .collect(Collectors.toList());
            }

            for (Result result : finalPodium) {
                Country country = athleteService.getAthleteById(result.getAthleteId()).get().getCountry();
                medalsByCountry.put(country, medalsByCountry.getOrDefault(country, 0) + 1);
            }
        }
        return medalsByCountry;
    }

    @Override
    public int calculateAverageParticipantAge(Olympiad olympiad) {
        return (int)athleteService.getAllAthletes().stream()
                .mapToInt(a -> athleteService.calcAge(a, olympiad))
                .average()
                .orElse(0);
    }

    @Override
    public Optional<Athlete> getYoungestMedalist(Olympiad olympiad) {
        List<Result> allMedalists = new ArrayList<>();

        for(Competition competition : olympiad.getCompetitions()){
            if(competition instanceof SkiSlalomCompetition){
                SkiSlalomCompetition slalom  = (SkiSlalomCompetition) competition;
                slalomService.calculateRankings(slalom);

                slalom.getResults().stream()
                        .filter(r -> !r.isDNF())
                        .limit(3)
                        .forEach(allMedalists::add);
            } else if(competition instanceof BiathlonCompetition){
                BiathlonCompetition biathlon  = (BiathlonCompetition) competition;
                biathlonService.calculateRankings(biathlon);

                biathlon.getResults().stream()
                        .filter(r -> !r.isDNF())
                        .limit(3)
                        .forEach(allMedalists::add);
            }
        }
        return allMedalists.stream()
                .map(r -> athleteService.getAthleteById(r.getAthleteId()).get())
                .min(Comparator.comparing(a -> athleteService.calcAge(a, olympiad)));
    }

    @Override
    public void exportStandingsToFile(Olympiad olympiad, String filePath) throws IOException {
        try(
                PrintWriter writer = new PrintWriter(new FileWriter(filePath))
        )
        {
            for(Competition competition : olympiad.getCompetitions()){
                List<? extends Result> ranking = null;
                ResultService resultService = null;
                String compName = "";

                if(competition instanceof SkiSlalomCompetition){
                    SkiSlalomCompetition slalom = (SkiSlalomCompetition) competition;
                    slalomService.calculateRankings(slalom);
                    ranking = slalomService.getResults(slalom);
                    resultService = slalomService.getResultService();
                    compName = "SKI SLALOM";
                } else if(competition instanceof BiathlonCompetition){
                    BiathlonCompetition biathlon = (BiathlonCompetition) competition;
                    biathlonService.calculateRankings(biathlon);
                    ranking = biathlonService.getResults(biathlon);
                    resultService = biathlonService.getResultService();
                    compName = "SKI SLALOM";
                }

                compName += " (" + competition.getSex() + " - Min Age: " + competition.getMinAge() + ")";

                if(ranking != null && resultService != null) {
                    writer.println("-----FINAL RANKING-----");
                    writer.println(compName);
                    if (ranking.isEmpty()) {
                        writer.println("No athletes successfully finished this competition.");
                    }

                    int place = 1;
                    for(Result result : ranking) {
                        Athlete athlete = athleteService.getAthleteById(result.getAthleteId())
                                .orElseThrow(() -> new AthleteDoesNotExist());
                        Duration time = resultService.calculateTotalTime(result, competition);
                        writer.println((place + ". " +
                                athlete.getName() + " - " +
                                athlete.getCountry() + " | " +
                                String.format("%03d:%03d", time.toSecondsPart(), time.toMillisPart())
                        ));
                        place++;
                    }
                }

                writer.println();
            }
        }
        catch (IOException e){
            System.out.println("Error writing ranking to file: " + e.getMessage());
        }
    }
}
