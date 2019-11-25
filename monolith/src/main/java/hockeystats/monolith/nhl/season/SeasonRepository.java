package hockeystats.monolith.nhl.season;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SeasonRepository extends ReactiveMongoRepository<Season, String> {
}
