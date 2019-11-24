package com.briandevinssuresh.hockeystats.monolith;

import brave.propagation.B3Propagation;
import brave.propagation.B3SingleFormat;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.Propagation;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.stats.StatsApi;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.suggest.SuggestApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
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
  OkHttpClient okHttpClient(List<Interceptor> interceptors) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    for (Interceptor interceptor : interceptors) {
      builder.addInterceptor(interceptor);
    }
    return builder.build();
  }

  @Bean
  StatsApi nhlStatsApi(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
    Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://statsapi.web.nhl.com")
        .addCallAdapterFactory(ReactorCallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build();

    return retrofit.create(StatsApi.class);
  }

  @Bean
  SuggestApi nhlSuggestApi(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
    Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://suggest.svc.nhl.com")
        .addCallAdapterFactory(ReactorCallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build();

    return retrofit.create(SuggestApi.class);
  }

  @Bean ExtraFieldPropagation.Factory extraFieldPropagation() {
    return ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
        .addRedactedField("retrofit-md5")
        .build();
  }
}
