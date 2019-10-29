package com.briandevinssures.hockeystats.scraper.nhl_api.suggest;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerSuggestion {
    long id;
    String familyName;
    String givenName;
    boolean active;
    boolean rookie;
    String height;
    Integer weight;
    String birthLocality;
    String birthRegion;
    String birthCountry;
    LocalDate birthDate;
    String team;
    String position;
    Integer number;
    String urlSuffix;
}
