package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class ScheduleDate {
  LocalDate date;
  List<ScheduleGame> games;
}
