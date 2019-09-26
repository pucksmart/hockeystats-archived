package com.briandevinssures.hockeystats.scraper;

import com.briandevinssures.hockeystats.scraper.nhl.player.PlayerSuggestionsProcessor;
import com.briandevinssures.hockeystats.scraper.nhl.player.PlayerSuggestionsReader;
import com.briandevinssures.hockeystats.scraper.nhl.player.PlayerWriter;
import com.briandevinssures.hockeystats.scraper.nhl.season.CatalogSeasonGamesStep;
import com.briandevinssures.hockeystats.scraper.nhl.season.CatalogSeasonsStep;
import com.briandevinssures.hockeystats.scraper.nhl.season.NhlGameSummary;
import com.briandevinssures.hockeystats.scraper.nhl.season.NhlSeason;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Schedule;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.ScheduleGame;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Seasons;
import com.briandevinssures.hockeystats.scraper.nhl_api.suggest.NhlSuggestApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.suggest.PlayerSuggestions;
import com.briandevinssures.hockeystats.scraper.player.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class ScraperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScraperApplication.class, args);
    }

    @Autowired
    JobBuilderFactory jobs;

    @Autowired
    StepBuilderFactory steps;

    @Bean
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    NhlStatsApi nhlStatsApi(ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://statsapi.web.nhl.com")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        return retrofit.create(NhlStatsApi.class);
    }

    @Bean
    NhlSuggestApi nhlSuggestApi(ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://suggest.svc.nhl.com")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        return retrofit.create(NhlSuggestApi.class);
    }

    @Bean
    Job catalogNhlSeasons(Step nhlSeasonsStep) {
        return jobs.get("catalog-nhl-seasons")
                .incrementer(new RunIdIncrementer())
                .start(nhlSeasonsStep)
                .build();
    }

    @Bean
    Job catalogNhlGames(Step nhlSeasonGamesStep) {
        return jobs.get("catalog-nhl-games")
                .start(nhlSeasonGamesStep)
                .build();
    }

    @Bean
    Step nhlSeasonsStep(CatalogSeasonsStep catalogSeasonsStep) {
        return steps.get("catalog-nhl-seasons")
                .tasklet(catalogSeasonsStep)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    Step playerScraperStep(PlayerSuggestionsReader reader, PlayerSuggestionsProcessor processor, PlayerWriter writer) {
        return steps.get("player-scraper")
                .<PlayerSuggestions, List<Player>>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    Step nhlSeasonGamesStep(CatalogSeasonGamesStep catalogSeasonGamesStep) {
        return steps.get("catalog-season-games")
                .<Schedule, List<NhlGameSummary>>chunk(1)
                .reader(catalogSeasonGamesStep)
                .processor(catalogSeasonGamesStep)
                .writer(catalogSeasonGamesStep)
                .build();
    }
}
