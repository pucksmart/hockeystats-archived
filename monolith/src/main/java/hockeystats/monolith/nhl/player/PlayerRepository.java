package hockeystats.monolith.nhl.player;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface PlayerRepository extends ReactiveMongoRepository<Player, String> {
  Mono<Player> findByNhlId(long nhlId);
}
