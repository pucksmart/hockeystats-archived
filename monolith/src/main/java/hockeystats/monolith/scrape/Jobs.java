package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl.game.GameStatus;
import hockeystats.monolith.nhl.game.GameType;
import hockeystats.monolith.nhl.game.Games;
import hockeystats.monolith.nhl.player.Players;
import hockeystats.monolith.nhl.season.Seasons;
import hockeystats.monolith.nhl_api.stats.Schedule;
import hockeystats.monolith.nhl_api.stats.ScheduleDate;
import hockeystats.monolith.nhl_api.stats.StatsSeasons;
import hockeystats.monolith.nhl_api.suggest.SuggestPlayers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import retrofit2.Response;

@Component
@Slf4j
@AllArgsConstructor
class Jobs {
  private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d)' (\\d{1,2})\"");

  private final ScrapePlayersApiConsumerTask scrapePlayersTask;
  private final Players players;
  private final ScrapeSeasonsApiConsumerTask scrapeSeasonsTask;
  private final Seasons seasons;
  private final ScrapeDaysGamesApiConsumerTask scrapeDaysGamesTask;
  private final Games games;

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

  @Scheduled(fixedRate = 60 * 60 * 1000)
  void playerScrapeJob() throws InterruptedException {
    scrapePlayersTask.run()
        //scrapePlayersTask.runWithFilter()
        .map(Response::body)
        .flatMapIterable(SuggestPlayers::getSuggestions)
        .flatMap(p -> players.getForNhlId(p.getId())
            .map(pe -> pe.setNhlId(p.getId())
                .setGivenName(p.getGivenName())
                .setFamilyName(p.getFamilyName())
                .setPosition(p.getPosition())
                .setBirthLocality(p.getBirthLocality())
                .setBirthRegion(p.getBirthRegion())
                .setBirthCountry(p.getBirthCountry())
                .setBirthDate(p.getBirthDate())
                .setHeight(heightToInches(p.getHeight()))
                .setWeight(p.getWeight())))
        .flatMap(players::save)
        .subscribeOn(Schedulers.elastic())
        .subscribe();
  }

  @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
  void seasonInfoScrapeJob() {
    scrapeSeasonsTask.run()
        //scrapeSeasonsTask.runWithFilter()
        .map(Response::body)
        .flatMapIterable(StatsSeasons::getSeasons)
        .flatMap(s -> seasons.getById(s.getSeasonId())
            .map(se -> se.setSeasonId(s.getSeasonId())
                .setRegularSeasonStartDate(s.getRegularSeasonStartDate())
                .setRegularSeasonEndDate(s.getRegularSeasonEndDate())
                .setSeasonEndDate(s.getSeasonEndDate())
                .setNumberOfGames(s.getNumberOfGames())
                .setTiesInUse(s.getTiesInUse())
                .setOlympicsParticipation(s.getOlympicsParticipation())
                .setConferencesInUse(s.getConferencesInUse())
                .setDivisionsInUse(s.getDivisionsInUse())
                .setWildCardInUse(s.getWildCardInUse())))
        .flatMap(seasons::save)
        .subscribeOn(Schedulers.elastic())
        .subscribe();
  }

  @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000, initialDelay = 5000)
  void gameScrapeJob() {
    scrapeDaysGamesTask.run()
        //scrapeDaysGamesTask.runWithFilter()
        .map(Response::body)
        .flatMapIterable(Schedule::getDates)
        .flatMapIterable(ScheduleDate::getGames)
        .flatMap(g -> games.getForId(g.getGamePk())
            .map(ge -> ge.setGameId(g.getGamePk())
                .setGameType(GameType.fromLetter(g.getGameType()))
                .setSeasonId(g.getSeason())
                .setStartAt(g.getGameDate())
                .setVenue(g.getVenue().getName())
                .setGameStatus(GameStatus.fromDescription(g.getStatus().getDetailedState()))
                .setAwayTeamId(g.getTeams().getAway().getTeam().getId())
                .setAwayScore(g.getTeams().getAway().getScore())
                .setHomeTeamId(g.getTeams().getHome().getTeam().getId())
                .setHomeScore(g.getTeams().getHome().getScore())))
        .flatMap(games::save)
        .subscribeOn(Schedulers.elastic())
        .subscribe();
  }
}
