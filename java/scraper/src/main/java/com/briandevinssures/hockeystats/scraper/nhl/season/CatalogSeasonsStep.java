package com.briandevinssures.hockeystats.scraper.nhl.season;

import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Seasons;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CatalogSeasonsStep implements Tasklet {

    private final NhlStatsApi nhlStatsApi;
    private final NhlSeasonRepository nhlSeasonRepository;
    private final JobLauncher jobLauncher;
//    private final Job seasonGames;

    CatalogSeasonsStep(NhlStatsApi nhlStatsApi, NhlSeasonRepository nhlSeasonRepository, JobLauncher jobLauncher) { //, @Qualifier("seasonGames") Job seasonGames) {
        this.nhlStatsApi = nhlStatsApi;
        this.nhlSeasonRepository = nhlSeasonRepository;
        this.jobLauncher = jobLauncher;
//        this.seasonGames = seasonGames;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Seasons seasons = getSeasons();
        List<NhlSeason> toPersist = seasons.getSeasons().stream().map(s -> {
            NhlSeason nhlSeason = new NhlSeason();
            BeanUtils.copyProperties(s, nhlSeason);
            return nhlSeason;
        }).collect(Collectors.toList());

        toPersist.forEach(nhlSeason -> {
            nhlSeasonRepository.save(nhlSeason);
//            try {
//                jobLauncher.run(seasonGames, new JobParameters(Collections.singletonMap("season.id", new JobParameter(nhlSeason.getSeasonId()))));
//            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException ignored) {
//            }
        });

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
