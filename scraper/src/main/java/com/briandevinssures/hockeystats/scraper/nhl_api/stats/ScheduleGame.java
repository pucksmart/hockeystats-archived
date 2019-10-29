package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

import java.time.Instant;

@Data
public class ScheduleGame {
    Long gamePk;
    String gameType;
    String season;
    Instant gameDate;
    ScheduleGameStatus status;
    ScheduleGameTeams teams;
    ScheduleGameVenue venue;
}
