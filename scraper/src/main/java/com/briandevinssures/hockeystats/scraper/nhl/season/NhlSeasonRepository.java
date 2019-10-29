package com.briandevinssures.hockeystats.scraper.nhl.season;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NhlSeasonRepository extends JpaRepository<NhlSeason, String> {
}
