package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

import java.util.List;

@Data
public class Schedule {
    List<ScheduleDate> dates;
}
