package com.briandevinssures.hockeystats.scraper.batch;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.LocalDate;
import java.util.Collections;

public class JobParametersIncrementers {
    public static final JobParametersIncrementer CURRENT_MONTH = parameters -> {
        LocalDate date = LocalDate.now();
        return new JobParameters(Collections.singletonMap("run.month", new JobParameter(String.format("%d-%02d", date.getYear(), date.getMonthValue()))));
    };

    public static final JobParametersIncrementer CURRENT_DAY = parameters -> new JobParameters(Collections.singletonMap("run.date", new JobParameter(LocalDate.now().toString())));
}
