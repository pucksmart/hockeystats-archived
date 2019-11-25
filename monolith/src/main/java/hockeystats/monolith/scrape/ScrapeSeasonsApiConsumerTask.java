package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl_api.stats.StatsApi;
import hockeystats.monolith.nhl_api.stats.StatsSeasons;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import retrofit2.Response;

@Component
class ScrapeSeasonsApiConsumerTask extends ApiConsumerTask<StatsSeasons> {
  private final StatsApi statsApi;

  ScrapeSeasonsApiConsumerTask(ResourceRepository resourceRepository, StatsApi statsApi) {
    super(resourceRepository);
    this.statsApi = statsApi;
  }

  @Override public Flux<Response<StatsSeasons>> run() {
    return Flux.from(statsApi.listSeasons());
  }
}
