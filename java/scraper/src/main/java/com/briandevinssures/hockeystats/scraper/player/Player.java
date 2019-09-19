package com.briandevinssures.hockeystats.scraper.player;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Builder(builderClassName = "Builder")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        schema = "alpha",
        name = "players"
)
public final class Player {
    @Id
    private long id;
    private String givenName;
    private String familyName;
    private String position;
    private String birthLocality;
    private String birthRegion;
    private String birthCountry;
    private LocalDate birthDate;
    private Integer height;
    private Integer weight;
}
