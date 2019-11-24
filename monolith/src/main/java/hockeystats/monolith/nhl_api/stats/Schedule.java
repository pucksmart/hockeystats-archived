package hockeystats.monolith.nhl_api.stats;

import hockeystats.monolith.nhl_api.ApiResponse;
import java.util.List;
import lombok.Data;

@Data
public class Schedule extends ApiResponse {
  List<ScheduleDate> dates;
}
