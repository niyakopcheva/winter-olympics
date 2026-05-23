package org.example.data.exceptions;

public class AthleteNotQualifiedException extends RuntimeException {
    public AthleteNotQualifiedException(){super("Athlete cannot enter!");}
    public AthleteNotQualifiedException(String message) {
        super(message);
    }
}
