package com.briandevinssures.hockeystats.scraper.player;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        schema = "alpha",
        name = "players",
        indexes = @Index(name = "nhl_id_index", columnList = "nhlId", unique = true)
)
@EntityListeners(AuditingEntityListener.class)
public final class Player {
    @Id @Builder.Default
    String id = UUID.randomUUID().toString();

    long nhlId;
    String givenName;
    String familyName;
    String position;
    String birthLocality;
    String birthRegion;
    String birthCountry;
    LocalDate birthDate;
    Integer height;
    Integer weight;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Instant modifiedDate;
}
