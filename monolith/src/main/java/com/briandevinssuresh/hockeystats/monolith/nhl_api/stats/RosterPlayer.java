package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import lombok.Data;

@Data
public class RosterPlayer {
  RosterPlayerInfo person;
  String jerseyNumber;
  RosterPlayerPosition position;
}
