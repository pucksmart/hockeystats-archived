package nhl.player.scraper;

import hockeystats.db.Tables;
import hockeystats.db.tables.daos.PlayersDao;
import hockeystats.db.tables.pojos.Players;
import hockeystats.db.tables.records.PlayersRecord;
import io.micronaut.configuration.picocli.PicocliRunner;
import java.sql.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.jooq.Condition;
import org.jooq.DSLContext;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import reactor.core.publisher.Mono;

@Command(name = "nhl-player-scraper", description = "...",
    mixinStandardHelpOptions = true)
public class NhlPlayerScraperCommand implements Runnable {

  public static void main(String[] args) throws Exception {
    PicocliRunner.run(NhlPlayerScraperCommand.class, args);
  }

  private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d)' (\\d{1,2})\"");

  @Option(names = {"-v", "--verbose"}, description = "...")
  boolean verbose;

  @Inject
  SuggestApi suggestApi;

  @Inject
  DSLContext dslContext;

  @Inject
  PlayersDao playersDao;

  public void run() {
    char[] query = new char[] {'a', 'a', 'a'};

    while (true) {
      suggestApi.suggestPlayers(new String(query))
          .flatMapIterable(SuggestPlayers::getSuggestions)
          .flatMap(p -> Mono.just(fromSuggestPlayer(p)))
          .subscribe();

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
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private PlayersRecord fromSuggestPlayer(SuggestPlayer suggestPlayer) {
    Players existing = playersDao.fetchOneById(suggestPlayer.getId());
    if (existing == null) {
      existing = new Players();
    }
    existing.setGivenName(suggestPlayer.getGivenName())
        .setFamilyName(suggestPlayer.getFamilyName())
        .setNumber(suggestPlayer.getNumber() == null ? null : suggestPlayer.getNumber().shortValue())
        .setPosition(suggestPlayer.getPosition())
        .setBirthDate(suggestPlayer.getBirthDate())
        .setBirthLocality(suggestPlayer.getBirthLocality())
        .setBirthRegion(suggestPlayer.getBirthRegion())
        .setBirthCountry(suggestPlayer.getBirthCountry())
        .setHeight(heightToInches(suggestPlayer.getHeight()))
        .setWeight(suggestPlayer.getWeight() == null ? null : suggestPlayer.getWeight().shortValue());

    return dslContext.newRecord(Tables.PLAYERS, existing);
  }

  private static Short heightToInches(String height) {
    if (height == null) {
      return null;
    }
    Matcher matcher = HEIGHT_PATTERN.matcher(height);
    if (matcher.matches()) {
      String feet = matcher.group(1);
      String inches = matcher.group(2);
      int heightInt = Integer.parseInt(feet) * 12 + Integer.parseInt(inches);
      return (short) heightInt;
    }
    return null;
  }
}
