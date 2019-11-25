package hockeystats.monolith.nhl;

import hockeystats.monolith.nhl.game.Game;
import hockeystats.monolith.nhl.player.Player;
import hockeystats.monolith.nhl.season.Season;
import hockeystats.monolith.scrape.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoIndexConfigurer {
  private static final Class[] DOCUMENT_CLASSES =
      new Class[] {Game.class, Player.class, Resource.class, Season.class};
  private final MongoTemplate mongoTemplate;
  private final MongoMappingContext mongoMappingContext;

  @EventListener(ApplicationReadyEvent.class)
  public void initIndicesAfterStartup() {
    IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

    for (Class c : DOCUMENT_CLASSES) {
      IndexOperations indexOps = mongoTemplate.indexOps(c);
      resolver.resolveIndexFor(c).forEach(indexOps::ensureIndex);
    }
  }
}
