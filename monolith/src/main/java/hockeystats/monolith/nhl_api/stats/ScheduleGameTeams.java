package hockeystats.monolith.nhl_api.stats;

import lombok.Data;

@Data
public class ScheduleGameTeams {
  ScheduleGameTeam away;
  ScheduleGameTeam home;
}
