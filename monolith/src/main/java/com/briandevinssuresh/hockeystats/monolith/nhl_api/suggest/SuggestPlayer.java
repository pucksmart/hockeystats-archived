package com.briandevinssuresh.hockeystats.monolith.nhl_api.suggest;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class SuggestPlayer {
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
