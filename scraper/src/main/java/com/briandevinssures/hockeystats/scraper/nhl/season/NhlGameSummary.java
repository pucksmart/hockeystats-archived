package com.briandevinssures.hockeystats.scraper.nhl.season;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(
        schema = "alpha",
        name = "nhl_game_summaries"
)
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Instant modifiedDate;
}
