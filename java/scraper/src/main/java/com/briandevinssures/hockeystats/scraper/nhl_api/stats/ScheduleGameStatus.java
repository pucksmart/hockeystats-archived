package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

@Data
public class ScheduleGameStatus {
    String abstractGameState;
    String codedGameState;
    String detailedState;
    String statusCode;
    Boolean startTimeTBD;
}
