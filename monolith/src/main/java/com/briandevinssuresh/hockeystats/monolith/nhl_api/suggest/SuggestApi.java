package com.briandevinssuresh.hockeystats.monolith.nhl_api.suggest;

import reactor.core.publisher.Mono;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SuggestApi {
  @GET("/svc/suggest/v1/minplayers/{searchQuery}/99999")
  Mono<Response<SuggestPlayers>> suggestPlayers(@Path("searchQuery") String searchQuery);
}
