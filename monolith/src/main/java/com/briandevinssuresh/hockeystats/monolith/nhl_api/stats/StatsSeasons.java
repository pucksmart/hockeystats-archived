package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatsSeasons {
    List<StatsSeason> seasons = new ArrayList<>();
}
