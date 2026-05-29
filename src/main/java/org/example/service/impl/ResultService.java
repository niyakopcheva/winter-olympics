package org.example.service.impl;

import org.example.data.Biathlon.BiathlonCompetition;
import org.example.data.Biathlon.BiathlonResult;
import org.example.data.Competition;
import org.example.data.Result;
import org.example.data.SkiSlalom.SlalomResult;
import org.example.service.IResultService;

import java.time.Duration;

public class ResultService implements IResultService {

    @Override
    public Duration calculateTotalTime(Result result, Competition competition) throws IllegalArgumentException{
        if(result == null || result.isDNF())
            return null;

        if(result instanceof SlalomResult){
            SlalomResult slalomResult = (SlalomResult) result;
            return slalomResult.getFirstRunTime().plus(slalomResult.getSecondRunTime());
        } else if(result instanceof BiathlonResult && competition instanceof BiathlonCompetition){
            BiathlonResult biathlonResult = (BiathlonResult) result;
            BiathlonCompetition biathlonCompetition = (BiathlonCompetition) competition;

            Duration penalty = biathlonCompetition.getTimePenalty().multipliedBy(biathlonResult.getMissedShots());
            return biathlonResult.getRunTime().plus(penalty);
        }

        throw new IllegalArgumentException("Unsupported combination of result and competition types.");
    }
}
