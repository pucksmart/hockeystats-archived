package com.briandevinssures.hockeystats.scraper.nhl.player;

import com.briandevinssures.hockeystats.scraper.nhl_api.suggest.NhlSuggestApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.suggest.PlayerSuggestions;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import retrofit2.Response;

@Component
public class PlayerSuggestionsReader implements ItemReader<PlayerSuggestions> {
    private final NhlSuggestApi nhlSuggestApi;

    private char[] query = new char[]{'a', 'a', 'a'};
    private boolean run = true;

    PlayerSuggestionsReader(NhlSuggestApi nhlSuggestApi) {
        this.nhlSuggestApi = nhlSuggestApi;
    }

    @Override
    public PlayerSuggestions read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!run) {
            return null;
        }

        PlayerSuggestions response = null;
        Response<PlayerSuggestions> playerSuggestions = nhlSuggestApi.suggestPlayers(new String(query)).execute();
        if (playerSuggestions.isSuccessful() && playerSuggestions.body() != null) {
            response = playerSuggestions.body();
        }

        if (query[0] == 'z' && query[1] == 'z' && query[2] == 'z') {
            run = false;
        } else {
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
        return response;
    }
}
