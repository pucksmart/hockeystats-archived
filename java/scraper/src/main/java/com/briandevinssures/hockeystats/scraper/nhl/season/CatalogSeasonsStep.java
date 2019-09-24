package com.briandevinssures.hockeystats.scraper.nhl.season;

import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Season;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Seasons;
import org.springframework.batch.item.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class CatalogSeasonsStep implements ItemReader<Seasons>, ItemProcessor<Seasons, List<NhlSeason>>, ItemWriter<List<NhlSeason>> {

    private final AtomicBoolean canRun = new AtomicBoolean(true);
    private final NhlStatsApi nhlStatsApi;
    private final NhlSeasonRepository nhlSeasonRepository;

    CatalogSeasonsStep(NhlStatsApi nhlStatsApi, NhlSeasonRepository nhlSeasonRepository) {
        this.nhlStatsApi = nhlStatsApi;
        this.nhlSeasonRepository = nhlSeasonRepository;
    }

    @Override
    public Seasons read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (canRun.getAndSet(false)) {
            Response<Seasons> seasons = nhlStatsApi.listSeasons().execute();

            if (seasons.isSuccessful() && seasons.body() != null && seasons.body().getSeasons() != null) {
                return seasons.body();
            } else {
                if (!seasons.isSuccessful()) {
                    throw new NonTransientResourceException("API call was not successful");
                }
                return seasons.body();
            }
        }
        return null;
    }

    @Override
    public List<NhlSeason> process(Seasons seasons) throws Exception {
        return seasons.getSeasons().stream().map(s -> {
            NhlSeason nhlSeason = new NhlSeason();
            BeanUtils.copyProperties(s, nhlSeason);
            return nhlSeason;
        }).collect(Collectors.toList());
    }

    @Override
    public void write(List<? extends List<NhlSeason>> list) throws Exception {
        list.forEach(nhlSeasonRepository::saveAll);
    }
}
