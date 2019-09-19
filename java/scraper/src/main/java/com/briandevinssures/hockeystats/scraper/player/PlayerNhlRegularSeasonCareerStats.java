package com.briandevinssures.hockeystats.scraper.player;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(fluent = true)
@Entity
@Table(
        schema = "alpha",
        name = "player_nhl_regular_season_career_stats"
)
public final class PlayerNhlRegularSeasonCareerStats {
    @Id
    private long id;
    private int gamesPlayed;
    private int goals;
    private int assists;
    private int points;
    private int gameWinningGoals;
    private int overtimeGoals;
    private int powerPlayGoals;
    private int powerPlayPoints;
    private int shortHandedGoals;
    private int shortHandedPoints;
    private int shotsOnGoal;
    private double shootingPercentage;
    private int plusMinus;
    private int penaltyMinutes;
}
