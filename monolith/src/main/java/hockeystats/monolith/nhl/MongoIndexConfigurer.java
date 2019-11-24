package hockeystats.monolith.nhl;

import hockeystats.monolith.nhl.player.Player;
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
  private final MongoTemplate mongoTemplate;
  private final MongoMappingContext mongoMappingContext;

  @EventListener(ApplicationReadyEvent.class)
  public void initIndicesAfterStartup() {
    IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

    IndexOperations indexOps = mongoTemplate.indexOps(Player.class);
    resolver.resolveIndexFor(Player.class).forEach(indexOps::ensureIndex);
  }
}
