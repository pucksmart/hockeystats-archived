package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl_api.stats.StatsApi;
import hockeystats.monolith.nhl_api.stats.StatsSeasons;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import retrofit2.Response;

@Component
@RequiredArgsConstructor
public class ScrapeSeasonsApiConsumerTask implements ApiConsumerTask<StatsSeasons> {
  private final StatsApi statsApi;

  @Override public Flux<Response<StatsSeasons>> run() {
    return Flux.from(statsApi.listSeasons());
  }
}
