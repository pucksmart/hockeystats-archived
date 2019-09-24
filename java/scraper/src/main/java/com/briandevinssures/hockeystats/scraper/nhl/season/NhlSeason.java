package com.briandevinssures.hockeystats.scraper.nhl.season;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(
        schema = "alpha",
        name = "nhl_seasons"
)
public class NhlSeason {
    @Id
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
