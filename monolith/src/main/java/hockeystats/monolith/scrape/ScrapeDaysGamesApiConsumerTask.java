package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl.season.Seasons;
import hockeystats.monolith.nhl_api.stats.Schedule;
import hockeystats.monolith.nhl_api.stats.StatsApi;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import retrofit2.Response;

@Component
@Slf4j
class ScrapeDaysGamesApiConsumerTask extends ApiConsumerTask<Schedule> {
  private final Seasons seasons;
  private final StatsApi statsApi;

  ScrapeDaysGamesApiConsumerTask(ResourceRepository resourceRepository, Seasons seasons,
      StatsApi statsApi) {
    super(resourceRepository);
    this.seasons = seasons;
    this.statsApi = statsApi;
  }

  @Override public Flux<Response<Schedule>> run() {
    return seasons.getAll()
        .flatMapIterable(s -> {
          LocalDate temp = s.getRegularSeasonStartDate();
          List<LocalDate> seasonDays = new ArrayList<>();
          seasonDays.add(temp);
          while (!temp.plusDays(1).isAfter(LocalDate.now())
              && !temp.plusDays(1).isAfter(s.getSeasonEndDate())) {
            temp = temp.plusDays(1);
            seasonDays.add(temp);
          }
          return seasonDays;
        })
        .map(LocalDate::toString)
        .delayElements(Duration.ofSeconds(2))
        .log()
        .flatMap(statsApi::getScheduleForDate);
  }
}
