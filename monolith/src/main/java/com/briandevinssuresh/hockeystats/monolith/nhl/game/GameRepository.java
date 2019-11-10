package com.briandevinssuresh.hockeystats.monolith.nhl.game;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, Long> {
}
