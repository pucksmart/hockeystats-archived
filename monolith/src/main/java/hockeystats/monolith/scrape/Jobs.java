package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl.game.Game;
import hockeystats.monolith.nhl.game.GameRepository;
import hockeystats.monolith.nhl.game.GameStatus;
import hockeystats.monolith.nhl.game.GameType;
import hockeystats.monolith.nhl.player.Player;
import hockeystats.monolith.nhl.player.PlayerRepository;
import hockeystats.monolith.nhl.season.Season;
import hockeystats.monolith.nhl.season.SeasonRepository;
import hockeystats.monolith.nhl_api.ApiResponse;
import hockeystats.monolith.nhl_api.stats.ScheduleDate;
import hockeystats.monolith.nhl_api.stats.StatsApi;
import hockeystats.monolith.nhl_api.suggest.SuggestPlayers;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retrofit2.Response;

@Component
@Slf4j
@AllArgsConstructor
class Jobs {
  private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d)' (\\d{1,2})\"");

  private final StatsApi statsApi;
  private final GameRepository gameRepository;
  private final PlayerRepository playerRepository;
  private final SeasonRepository seasonRepository;
  private final ResourceRepository resourceRepository;
  private final ScrapePlayersJob scrapePlayersJob;

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

  private Mono<Boolean> apiResponseUpdated(Response<? extends ApiResponse> apiResponse) {
    if (apiResponse == null || apiResponse.body() == null) {
      return Mono.just(false);
    }
    String url = apiResponse.raw().request().url().toString();
    String hash = apiResponse.body().getHash();

    return resourceRepository.findById(url)
        .defaultIfEmpty(new Resource())
        .map(r -> {
          int existingHashCode = r.hashCode();
          r.setUrl(url)
              .setMd5(hash)
              .setJson(new String(apiResponse.body().getJson()));
          if (existingHashCode == r.hashCode()) {
            return false;
          } else {
            resourceRepository.save(r).subscribe();
            return true;
          }
        });
  }

  @Scheduled(fixedRate = 60 * 60 * 1000)
  void playerScrapeJob() throws InterruptedException {
    scrapePlayersJob.run()
        .filterWhen(this::apiResponseUpdated)
        .map(Response::body)
        .flatMapIterable(SuggestPlayers::getSuggestions)
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
        .flatMap(p -> {
          try {
            return playerRepository.save(p);
          } catch (OptimisticLockingFailureException e) {
            return Mono.empty();
          }
        })
        .subscribe();
  }

  //  @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
  void seasonInfoScrapeJob() {
    seasonRepository.saveAll(
        statsApi.listSeasons()
            .flatMapIterable(s -> s.body().getSeasons())
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
            .flatMapIterable(s -> s.body().getDates())
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
