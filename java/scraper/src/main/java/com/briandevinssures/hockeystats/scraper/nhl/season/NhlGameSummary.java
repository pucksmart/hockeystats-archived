package com.briandevinssures.hockeystats.scraper.nhl.season;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Table(
        schema = "alpha",
        name = "nhl_game_summaries"
)
public class NhlGameSummary {
    @Id
    long gameId;

    String gameType;
    String seasonId;
    Instant gameDate;
    String venue;
    String gameState;

    long awayTeamId;
    int awayScore;
    long homeTeamId;
    int homeScore;
}
