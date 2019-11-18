package nhl.player.scraper;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("https://suggest.svc.nhl.com")
public interface SuggestApi {
  @Get("/svc/suggest/v1/minplayers/{searchQuery}/99999")
  Mono<SuggestPlayers> suggestPlayers(String searchQuery);
}
