package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

@Data
public class ScheduleGameTeams {
    ScheduleGameTeam away;
    ScheduleGameTeam home;
}
