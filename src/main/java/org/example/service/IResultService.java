package org.example.service;

import org.example.data.Competition;
import org.example.data.Result;

import java.time.Duration;

public interface IResultService {
    Duration calculateTotalTime(Result result, Competition competition);
}
