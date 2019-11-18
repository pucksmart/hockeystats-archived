package hockeystats.scrape.nhl.player;

import hockeystats.db.Tables;
import hockeystats.db.tables.records.PlayersRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.impl.UpdatableRecordImpl;

@Singleton
public class Job implements Runnable {
  private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d)' (\\d{1,2})\"");

  @Inject
  public SuggestApi suggestApi;

  @Inject
  public DSLContext dslContext;

  @Override public void run() {
    char[] query = new char[] {'a', 'a', 'a'};

    while (true) {
      suggestApi.suggestPlayers(new String(query))
          .flatMapIterable(SuggestPlayers::getSuggestions)
          .log()
          .map(this::fromSuggestPlayer)
          .map(UpdatableRecordImpl::store)
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
    PlayersRecord existing = dslContext
        .selectFrom(Tables.PLAYERS)
        .where(Tables.PLAYERS.ID.eq(suggestPlayer.getId()))
        .fetchOne();
    if (existing == null) {
      existing = Tables.PLAYERS.newRecord().setId(suggestPlayer.getId());
    }
    return existing.setGivenName(suggestPlayer.getGivenName())
        .setFamilyName(suggestPlayer.getFamilyName())
        .setNumber(suggestPlayer.getNumber() == null ? null : suggestPlayer.getNumber().shortValue())
        .setPosition(suggestPlayer.getPosition())
        .setBirthDate(suggestPlayer.getBirthDate())
        .setBirthLocality(suggestPlayer.getBirthLocality())
        .setBirthRegion(suggestPlayer.getBirthRegion())
        .setBirthCountry(suggestPlayer.getBirthCountry())
        .setHeight(heightToInches(suggestPlayer.getHeight()))
        .setWeight(suggestPlayer.getWeight() == null ? null : suggestPlayer.getWeight().shortValue());
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
