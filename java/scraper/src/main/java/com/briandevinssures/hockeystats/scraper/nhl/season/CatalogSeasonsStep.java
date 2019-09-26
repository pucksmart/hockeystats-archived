package com.briandevinssures.hockeystats.scraper.nhl.season;

import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Season;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Seasons;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class CatalogSeasonsStep implements Tasklet {

    private final NhlStatsApi nhlStatsApi;
    private final NhlSeasonRepository nhlSeasonRepository;

    CatalogSeasonsStep(NhlStatsApi nhlStatsApi, NhlSeasonRepository nhlSeasonRepository) {
        this.nhlStatsApi = nhlStatsApi;
        this.nhlSeasonRepository = nhlSeasonRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Seasons seasons = getSeasons();
        List<NhlSeason> toPersist = seasons.getSeasons().stream().map(s -> {
            NhlSeason nhlSeason = new NhlSeason();
            BeanUtils.copyProperties(s, nhlSeason);
            return nhlSeason;
        }).collect(Collectors.toList());
        nhlSeasonRepository.saveAll(toPersist);
        return RepeatStatus.FINISHED;
    }

    private Seasons getSeasons() throws IOException {
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
}
