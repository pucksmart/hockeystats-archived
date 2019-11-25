package hockeystats.monolith.scrape;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@EqualsAndHashCode
@Document("resources")
public class Resource {
  @Id
  private String url;
  private String md5;

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
