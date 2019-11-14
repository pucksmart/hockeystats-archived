package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import lombok.Data;

@Data
public class RosterPlayerPosition {
  String code;
  String name;
  String type;
  String abbreviation;
}
