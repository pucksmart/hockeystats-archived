package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl_api.suggest.SuggestApi;
import hockeystats.monolith.nhl_api.suggest.SuggestPlayers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import retrofit2.Response;

@Component
class ScrapePlayersApiConsumerTask extends ApiConsumerTask<SuggestPlayers> {
  private final SuggestApi suggestApi;

  ScrapePlayersApiConsumerTask(ResourceRepository resourceRepository, SuggestApi suggestApi) {
    super(resourceRepository);
    this.suggestApi = suggestApi;
  }

  @Override public Flux<Response<SuggestPlayers>> run() {
    return Flux.just("")
        .flatMap(this::appendFullAlphabet)
        .flatMap(this::appendFullAlphabet)
        .flatMap(this::appendFullAlphabet)
        .delayElements(Duration.ofMillis(250))
        .log()
        .flatMap(suggestApi::suggestPlayers);
  }

  private Flux<String> appendFullAlphabet(String in) {
    List<String> iter = new ArrayList<>();
    for (char i = 'a'; i <= 'z'; i++) {
      iter.add(in + i);
    }
    return Flux.fromIterable(iter);
  }
}
