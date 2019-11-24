package hockeystats.monolith.nhl_api.stats;

import lombok.Data;

@Data
public class ScheduleGameStatus {
  String abstractGameState;
  String codedGameState;
  String detailedState;
  String statusCode;
  Boolean startTimeTBD;
}
