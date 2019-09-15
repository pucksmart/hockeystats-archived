package com.briandevinssures.hockeystats.scraper.player;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder(builderClassName = "Builder")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        schema = "alpha",
        name = "players",
        indexes = @Index(name = "nhl_id_index", columnList = "nhlId", unique = true)
)
public final class Player {
    @Id
    @lombok.Builder.Default
    private final UUID id = UUID.randomUUID();
    private String givenName;
    private String familyName;
    private long nhlId;
}
