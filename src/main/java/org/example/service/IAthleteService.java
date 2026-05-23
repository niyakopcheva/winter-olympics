package org.example.service;

import org.example.data.Athlete;
import org.example.data.Competition;
import org.example.data.Olympiad;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IAthleteService {
    boolean registerAthlete(Athlete athlete);
    Optional<Athlete> getAthleteById(UUID id);
    Collection<Athlete> getAllAthletes();
    boolean isEligibleForCompetition(UUID athleteId, Competition competition, Olympiad olympiad);
    int calcAge(Athlete athlete, Olympiad olympiad);
}
