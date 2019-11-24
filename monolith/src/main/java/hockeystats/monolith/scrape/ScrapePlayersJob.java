package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl_api.ApiResponse;
import hockeystats.monolith.nhl_api.suggest.SuggestApi;
import hockeystats.monolith.nhl_api.suggest.SuggestPlayers;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import retrofit2.Response;

@Component
@RequiredArgsConstructor
public class ScrapePlayersJob implements Job<SuggestPlayers> {
  private final SuggestApi suggestApi;

  @Override public Flux<Response<SuggestPlayers>> run() {
    List<String> queries = new ArrayList<>();
    char[] query = new char[] {'a', 'a', 'a'};

    while(query[0] != 'z' || query[1] != 'z' || query[2] != 'z') {
      queries.add(new String(query));
      query[2]++;
      if (query[2] > 'z') {
        query[2] = 'a';
        query[1]++;
      }
      if (query[1] > 'z') {
        query[1] = 'a';
        query[0]++;
      }
    }
    queries.add(new String(query));

    return Flux.fromIterable(queries)
        .log(ScrapePlayersJob.class.getName())
        .flatMap(suggestApi::suggestPlayers);
  }
}
