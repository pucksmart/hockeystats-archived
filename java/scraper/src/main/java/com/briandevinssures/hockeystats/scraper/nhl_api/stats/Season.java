package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Season {
    String seasonId;
    LocalDate regularSeasonStartDate;
    LocalDate regularSeasonEndDate;
    LocalDate seasonEndDate;
    Integer numberOfGames;
    Boolean tiesInUse;
    Boolean olympicsParticipation;
    Boolean conferencesInUse;
    Boolean divisionsInUse;
    Boolean wildCardInUse;
}
