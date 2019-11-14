package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import java.util.List;
import lombok.Data;

@Data
public class Schedule {
  List<ScheduleDate> dates;
}
