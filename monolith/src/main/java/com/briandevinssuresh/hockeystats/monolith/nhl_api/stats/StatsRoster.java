package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import java.util.List;
import lombok.Data;

@Data
public class StatsRoster {
  List<RosterPlayer> roster;
}
