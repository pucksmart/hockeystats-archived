package hockeystats.monolith.nhl.game;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Document("games")
public class Game {
  @Id
  private long gameId;

  private GameType gameType;
  private String seasonId;
  private Instant startAt;
  private String venue;
  private GameStatus gameStatus;

  private long awayTeamId;
  private int awayScore;
  private long homeTeamId;
  private int homeScore;

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
