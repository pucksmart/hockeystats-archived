package com.briandevinssures.hockeystats.scraper.nhl.season;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NhlGameSummaryRepository extends JpaRepository<NhlGameSummary, Long> {
}
