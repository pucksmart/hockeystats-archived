package hockeystats.monolith.nhl.season;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

@EqualsAndHashCode(callSuper = false)
class WrappedSeason extends Season {
  @JsonIgnore
  @Getter(AccessLevel.PACKAGE)
  @Delegate
  private final Season delegate;

  @Getter(AccessLevel.PACKAGE)
  private final int originalDelegateHashCode;

  WrappedSeason(Season delegate) {
    this.delegate = delegate;
    this.originalDelegateHashCode = delegate.hashCode();
  }
}
