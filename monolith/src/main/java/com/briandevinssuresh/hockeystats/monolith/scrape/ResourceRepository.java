package com.briandevinssuresh.hockeystats.monolith.scrape;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends ReactiveMongoRepository<Resource, String> {
}
