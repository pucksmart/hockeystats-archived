package hockeystats.monolith.nhl.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

@EqualsAndHashCode(callSuper = false)
class WrappedPlayer extends Player {
  @JsonIgnore
  @Getter(AccessLevel.PACKAGE)
  @Delegate
  private final Player delegate;

  @Getter(AccessLevel.PACKAGE)
  private final int originalDelegateHashCode;

  WrappedPlayer(Player delegate) {
    this.delegate = delegate;
    this.originalDelegateHashCode = delegate.hashCode();
  }
}
