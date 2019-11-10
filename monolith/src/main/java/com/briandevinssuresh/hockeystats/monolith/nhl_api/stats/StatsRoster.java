package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import lombok.Data;

import java.util.List;

@Data
public class StatsRoster {
    List<RosterPlayer> roster;
}
