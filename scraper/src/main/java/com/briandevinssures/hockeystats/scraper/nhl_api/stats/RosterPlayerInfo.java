package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

@Data
public class RosterPlayerInfo {
    long id;
    String fullName;
    String link;
}
