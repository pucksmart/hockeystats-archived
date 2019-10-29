package com.briandevinssures.hockeystats.scraper;

import com.briandevinssures.hockeystats.scraper.batch.JobParametersIncrementers;
import com.briandevinssures.hockeystats.scraper.nhl.season.CatalogSeasonGamesStep;
import com.briandevinssures.hockeystats.scraper.nhl.season.CatalogSeasonsStep;
import com.briandevinssures.hockeystats.scraper.nhl.season.NhlGameSummary;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Schedule;
import com.briandevinssures.hockeystats.scraper.nhl_api.suggest.NhlSuggestApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JobsConfiguration extends BasicBatchConfigurer {
    @Autowired
    JobBuilderFactory jobs;

    @Autowired
    StepBuilderFactory steps;

    protected JobsConfiguration(BatchProperties properties, DataSource dataSource, TransactionManagerCustomizers transactionManagerCustomizers) {
        super(properties, dataSource, transactionManagerCustomizers);
    }

    @Override
    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
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
                .incrementer(JobParametersIncrementers.CURRENT_MONTH)
                .start(nhlSeasonsStep)
                .build();
    }

//    @Bean
//    Job seasonGames(CatalogSeasonGamesStep catalogSeasonGamesStep) {
//        return jobs.get("catalog-nhl-games")
//                .start(catalogSeasonGamesStep)
//                .build();
//    }

    @Bean
    Step nhlSeasonsStep(CatalogSeasonsStep catalogSeasonsStep) {
        return steps.get("catalog-nhl-seasons")
                .tasklet(catalogSeasonsStep)
                .allowStartIfComplete(true)
                .build();
    }
}
