package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import lombok.Data;

import java.util.List;

@Data
public class Schedule {
    List<ScheduleDate> dates;
}
