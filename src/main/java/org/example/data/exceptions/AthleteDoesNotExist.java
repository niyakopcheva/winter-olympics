package org.example.data.exceptions;

public class AthleteDoesNotExist extends RuntimeException {
    public AthleteDoesNotExist() {
        super("Athlete is not registered!");
    }
    public AthleteDoesNotExist(String message) {
        super(message);
    }
}
