package com.briandevinssures.hockeystats.scraper;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaAuditing
@EnableScheduling
public class ScraperApplication extends JobLauncherCommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ScraperApplication.class, args);
    }

    @Autowired
    public ScraperApplication(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository) {
        super(jobLauncher, jobExplorer, jobRepository);
    }

    @Override
    protected void execute(Job job, JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobParametersNotFoundException {
        try {
            super.execute(job, jobParameters);
        } catch (Exception ignored) {
        }
    }
}
