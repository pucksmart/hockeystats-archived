package com.briandevinssuresh.hockeystats.monolith.nhl_api.stats;

import reactor.core.publisher.Mono;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StatsApi {
  @GET("/api/v1/seasons")
  Mono<StatsSeasons> listSeasons();

  @GET("/api/v1/schedule")
  Mono<Schedule> getScheduleForDate(@Query("date") String date);

  @GET("/api/v1/teams/{teamId}/roster")
  Mono<StatsRoster> getTeamRoster(@Path("teamId") long teamId, @Query("season") String seasonId);
}
