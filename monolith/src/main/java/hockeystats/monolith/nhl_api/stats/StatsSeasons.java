package hockeystats.monolith.nhl_api.stats;

import hockeystats.monolith.nhl_api.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class StatsSeasons extends ApiResponse {
  List<StatsSeason> seasons = new ArrayList<>();
}
