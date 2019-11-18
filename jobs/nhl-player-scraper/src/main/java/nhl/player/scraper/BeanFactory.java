package nhl.player.scraper;

import hockeystats.db.tables.daos.PlayersDao;
import io.micronaut.context.annotation.Factory;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Factory
public class BeanFactory {
  @Singleton PlayersDao playersDao(DSLContext dslContext) {
    return new PlayersDao(dslContext.configuration());
  }
}
