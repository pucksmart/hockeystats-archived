package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

@Data
public class RosterPlayer {
    RosterPlayerInfo person;
    String jerseyNumber;
    RosterPlayerPosition position;
}
