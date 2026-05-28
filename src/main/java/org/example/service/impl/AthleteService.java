package org.example.service.impl;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;
import org.example.data.exceptions.AthleteDoesNotExist;
import org.example.service.IAthleteService;

import java.time.Period;
import java.util.*;

public class AthleteService implements IAthleteService {
    private final Set<Athlete> athletes;

    public AthleteService() {
        this.athletes = new HashSet<>();
    }

    @Override
    public boolean registerAthlete(Athlete athlete) {
        return athletes.add(athlete);
    }

    @Override
    public Optional<Athlete> getAthleteById(UUID id) {
        return athletes.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public Collection<Athlete> getAllAthletes() {
        return athletes;
    }

    @Override
    public boolean isEligibleForCompetition(UUID athleteId, Competition competition, Olympiad olympiad) {
        Optional<Athlete> athleteOpt = getAthleteById(athleteId);
        if(athleteOpt.isEmpty())
            throw new AthleteDoesNotExist();

        Athlete athlete = athleteOpt.get();
        return athlete.getSex() == competition.getSex() && calcAge(athlete, olympiad) >= competition.getMinAge();
    }

    @Override
    public int calcAge(Athlete athlete, Olympiad olympiad) {
        return Period.between(athlete.getBirthDate(), olympiad.getStartDate()).getYears();
    }
}
