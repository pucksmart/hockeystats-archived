package com.briandevinssures.hockeystats.scraper.player;

import lombok.Data;

import java.util.List;

@Data
public class StatsApiResponse {
    String copyright;
    List<StatSplits> stats;

    @Data
    public static class StatSplits {
        List<Split> splits;
        Type type;
    }

    @Data
    public static class Split {
        Stat stat;
    }

    @Data
    public static class Type {
        String displayName;
    }

    @Data
    public static class Stat {
        int gamesPlayed;
        int goals;
        int assists;
        int points;
        int gameWinningGoals;
        int overtimeGoals;
        int powerPlayGoals;
        int powerPlayPoints;
        int shortHandedGoals;
        int shortHandedPoints;
        int shotsOnGoal;
        double shootingPercentage;
        int plusMinus;
        int penaltyMinutes;
    }
}
