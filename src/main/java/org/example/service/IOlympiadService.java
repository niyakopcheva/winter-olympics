package org.example.service;

import org.example.data.Athlete;
import org.example.data.Olympiad;
import org.example.data.enums.Country;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface IOlympiadService {
    Map<Country, Integer> getMedalCountByCountry(Olympiad olympiad);
    int calculateAverageParticipantAge(Olympiad olympiad);
    Optional<Athlete> getYoungestMedalist(Olympiad olympiad);
    void exportStandingsToFile(Olympiad olympiad, String filePath) throws IOException;
}
