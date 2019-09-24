package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Seasons {
    List<Season> seasons = new ArrayList<>();
}
