package hockeystats.monolith.nhl.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

@EqualsAndHashCode(callSuper = false)
class WrappedGame extends Game {
  @JsonIgnore
  @Getter(AccessLevel.PACKAGE)
  @Delegate
  private final Game delegate;

  @Getter(AccessLevel.PACKAGE)
  private final int originalDelegateHashCode;

  WrappedGame(Game delegate) {
    this.delegate = delegate;
    this.originalDelegateHashCode = delegate.hashCode();
  }
}
