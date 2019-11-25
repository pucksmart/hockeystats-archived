package hockeystats.monolith.nhl.player;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Document("players")
public class Player {
  @Id
  @Setter(AccessLevel.PACKAGE)
  private String id = UUID.randomUUID().toString();

  @Indexed(unique = true)
  private long nhlId;
  private String givenName;
  private String familyName;
  private Integer number;
  private String position;
  private String birthLocality;
  private String birthRegion;
  private String birthCountry;
  private LocalDate birthDate;
  private Integer height;
  private Integer weight;

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
