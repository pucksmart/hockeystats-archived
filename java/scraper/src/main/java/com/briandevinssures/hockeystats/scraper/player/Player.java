package com.briandevinssures.hockeystats.scraper.player;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
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
}
