package com.briandevinssuresh.hockeystats.monolith.nhl.player;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document("players")
public class Player {
    @Id
    @Builder.Default
    String id = UUID.randomUUID().toString();

    @Indexed(unique = true)
    long nhlId;
    String givenName;
    String familyName;
    Integer number;
    String position;
    String birthLocality;
    String birthRegion;
    String birthCountry;
    LocalDate birthDate;
    Integer height;
    Integer weight;

    @Setter(AccessLevel.PACKAGE)
    @Version
    private Long version;

    @Setter(AccessLevel.PACKAGE)
    @CreatedDate
    private Instant created;

    @Setter(AccessLevel.PACKAGE)
    @LastModifiedDate
    private Instant lastModified;
}
