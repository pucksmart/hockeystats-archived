package com.briandevinssuresh.hockeystats.monolith;

import com.briandevinssuresh.hockeystats.monolith.nhl_api.stats.StatsApi;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.suggest.SuggestApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class MonolithApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonolithApplication.class, args);
  }

  @Bean
  StatsApi nhlStatsApi(ObjectMapper objectMapper) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://statsapi.web.nhl.com")
        .addCallAdapterFactory(ReactorCallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build();

    return retrofit.create(StatsApi.class);
  }

  @Bean
  SuggestApi nhlSuggestApi(ObjectMapper objectMapper) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://suggest.svc.nhl.com")
        .addCallAdapterFactory(ReactorCallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build();

    return retrofit.create(SuggestApi.class);
  }
}
