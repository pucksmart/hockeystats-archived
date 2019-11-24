package com.briandevinssuresh.hockeystats.monolith.scrape;

import brave.propagation.ExtraFieldPropagation;
import com.briandevinssuresh.hockeystats.monolith.OkResponseHashInterceptor;
import com.briandevinssuresh.hockeystats.monolith.nhl.game.Game;
import com.briandevinssuresh.hockeystats.monolith.nhl.game.GameRepository;
import com.briandevinssuresh.hockeystats.monolith.nhl.game.GameStatus;
import com.briandevinssuresh.hockeystats.monolith.nhl.game.GameType;
import com.briandevinssuresh.hockeystats.monolith.nhl.player.Player;
import com.briandevinssuresh.hockeystats.monolith.nhl.player.PlayerRepository;
import com.briandevinssuresh.hockeystats.monolith.nhl.season.Season;
import com.briandevinssuresh.hockeystats.monolith.nhl.season.SeasonRepository;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.stats.Schedule;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.stats.ScheduleDate;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.stats.StatsApi;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.stats.StatsSeasons;
import com.briandevinssuresh.hockeystats.monolith.nhl_api.suggest.SuggestApi;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@AllArgsConstructor
class Jobs {
  private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d)' (\\d{1,2})\"");

  private final StatsApi statsApi;
  private final SuggestApi suggestApi;
  private final GameRepository gameRepository;
  private final PlayerRepository playerRepository;
  private final SeasonRepository seasonRepository;
  private final ResourceRepository resourceRepository;

  private static Integer heightToInches(String height) {
    if (height == null) {
      return null;
    }
    Matcher matcher = HEIGHT_PATTERN.matcher(height);
    if (matcher.matches()) {
      String feet = matcher.group(1);
      String inches = matcher.group(2);
      return Integer.parseInt(feet) * 12 + Integer.parseInt(inches);
    }
    return null;
  }

  @Scheduled(fixedRate = 60 * 60 * 1000, initialDelay = 2000)
  void playerScrapeJob() throws InterruptedException {
    char[] query = new char[] {'a', 'a', 'a'};

    while (true) {
      String queryString = new String(query);
      log.info(queryString);
      playerRepository.saveAll(
          suggestApi.suggestPlayers(queryString)
              .flatMapIterable(s -> {
                if (ExtraFieldPropagation.getAll().size() > 0) {
                  String url = s.raw().request().url().toString();
                  String md5 = ExtraFieldPropagation.get("retrofit-md5");
                  Resource existing = resourceRepository.findById(url).block();
                  Resource.ResourceBuilder builder = existing != null ? existing.toBuilder() : Resource.builder();
                  Resource resource = builder.url(url).md5(md5).build();
                  resourceRepository.save(resource).block();
                }
                return s.body().getSuggestions();
              })
              .flatMap(p -> playerRepository.findByNhlId(p.getId())
                  .defaultIfEmpty(Player.builder().build())
                  .map(Player::toBuilder)
                  .map(pe -> pe.nhlId(p.getId())
                      .givenName(p.getGivenName())
                      .familyName(p.getFamilyName())
                      .position(p.getPosition())
                      .birthLocality(p.getBirthLocality())
                      .birthRegion(p.getBirthRegion())
                      .birthCountry(p.getBirthCountry())
                      .birthDate(p.getBirthDate())
                      .height(heightToInches(p.getHeight()))
                      .weight(p.getWeight())
                      .build()))
      ).subscribe();

      if (query[0] == 'z' && query[1] == 'z' && query[2] == 'z') {
        break;
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
      Thread.sleep(400);
    }
  }

  //  @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
  void seasonInfoScrapeJob() {
    seasonRepository.saveAll(
        statsApi.listSeasons()
            .flatMapIterable(StatsSeasons::getSeasons)
            .flatMap(s -> seasonRepository.findById(s.getSeasonId())
                .defaultIfEmpty(Season.builder().build())
                .map(Season::toBuilder)
                .map(se -> se.seasonId(s.getSeasonId())
                    .regularSeasonStartDate(s.getRegularSeasonStartDate())
                    .regularSeasonEndDate(s.getRegularSeasonEndDate())
                    .seasonEndDate(s.getSeasonEndDate())
                    .numberOfGames(s.getNumberOfGames())
                    .tiesInUse(s.getTiesInUse())
                    .olympicsParticipation(s.getOlympicsParticipation())
                    .conferencesInUse(s.getConferencesInUse())
                    .divisionsInUse(s.getDivisionsInUse())
                    .wildCardInUse(s.getWildCardInUse())
                    .build()
                )
            )
    ).subscribe();
  }

  //  @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000, initialDelay = 2000)
  void gameScrapeJob() {
    gameRepository.saveAll(
        seasonRepository.findAll()
            .flatMap(s -> {
              LocalDate temp = s.getRegularSeasonStartDate();
              List<LocalDate> seasonDays = new ArrayList<>();
              seasonDays.add(temp);
              while (!temp.plusDays(1).isAfter(s.getSeasonEndDate())) {
                temp = temp.plusDays(1);
                seasonDays.add(temp);
              }
              return Flux.fromIterable(seasonDays);
            })
            .flatMap(d -> statsApi.getScheduleForDate(d.toString()))
            .delayElements(Duration.of(500, ChronoUnit.MILLIS))
            .flatMapIterable(Schedule::getDates)
            .flatMapIterable(ScheduleDate::getGames)
            .flatMap(g -> gameRepository.findById(g.getGamePk())
                .defaultIfEmpty(Game.builder().build())
                .map(Game::toBuilder)
                .map(ge -> ge.gameId(g.getGamePk())
                    .gameType(GameType.fromLetter(g.getGameType()))
                    .seasonId(g.getSeason())
                    .startAt(g.getGameDate())
                    .venue(g.getVenue().getName())
                    .gameStatus(GameStatus.fromDescription(g.getStatus().getDetailedState()))
                    .awayTeamId(g.getTeams().getAway().getTeam().getId())
                    .awayScore(g.getTeams().getAway().getScore())
                    .homeTeamId(g.getTeams().getHome().getTeam().getId())
                    .homeScore(g.getTeams().getHome().getScore())
                    .build()
                )
            )
    ).subscribe();
  }
}
