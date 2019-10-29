package com.briandevinssures.hockeystats.scraper.nhl_api.suggest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NhlSuggestApi {
    @GET("/svc/suggest/v1/minplayers/{searchQuery}/99999")
    Call<PlayerSuggestions> suggestPlayers(@Path("searchQuery") String searchQuery);
}
