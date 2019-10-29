package com.briandevinssures.hockeystats.scraper.nhl_api.stats;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NhlStatsApi {
    @GET("/api/v1/seasons")
    Call<Seasons> listSeasons();

    @GET("/api/v1/schedule")
    Call<Schedule> getSchedule(@Query("startDate") String startDate, @Query("endDate") String endDate);

    @GET("/api/v1/teams/{teamId}/roster")
    Call<Roster> getTeamRoster(@Path("teamId") long teamId, @Query("season") String seasonId);
}
