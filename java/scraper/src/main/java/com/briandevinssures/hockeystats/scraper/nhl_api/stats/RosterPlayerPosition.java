package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

@Data
public class RosterPlayerPosition {
    String code;
    String name;
    String type;
    String abbreviation;
}
