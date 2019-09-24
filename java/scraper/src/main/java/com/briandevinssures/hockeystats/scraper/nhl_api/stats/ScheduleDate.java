package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleDate {
    LocalDate date;
    List<ScheduleGame> games;
}
