package hockeystats.monolith.nhl.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
class WrappedGame extends Game {
  @JsonIgnore
  @Getter(AccessLevel.PACKAGE)
  private final Game delegate;

  @Getter(AccessLevel.PACKAGE)
  private final int originalDelegateHashCode;

  WrappedGame(Game delegate) {
    this.delegate = delegate;
    this.originalDelegateHashCode = delegate.hashCode();
  }

  @Override public long getGameId() {
    return delegate.getGameId();
  }

  @Override public GameType getGameType() {
    return delegate.getGameType();
  }

  @Override public String getSeasonId() {
    return delegate.getSeasonId();
  }

  @Override public Instant getStartAt() {
    return delegate.getStartAt();
  }

  @Override public String getVenue() {
    return delegate.getVenue();
  }

  @Override public GameStatus getGameStatus() {
    return delegate.getGameStatus();
  }

  @Override public long getAwayTeamId() {
    return delegate.getAwayTeamId();
  }

  @Override public int getAwayScore() {
    return delegate.getAwayScore();
  }

  @Override public long getHomeTeamId() {
    return delegate.getHomeTeamId();
  }

  @Override public int getHomeScore() {
    return delegate.getHomeScore();
  }

  @Override public Game setGameId(long gameId) {
    delegate.setGameId(gameId);
    return this;
  }

  @Override public Game setGameType(GameType gameType) {
    delegate.setGameType(gameType);
    return this;
  }

  @Override public Game setSeasonId(String seasonId) {
    delegate.setSeasonId(seasonId);
    return this;
  }

  @Override public Game setStartAt(Instant startAt) {
    delegate.setStartAt(startAt);
    return this;
  }

  @Override public Game setVenue(String venue) {
    delegate.setVenue(venue);
    return this;
  }

  @Override public Game setGameStatus(GameStatus gameStatus) {
    delegate.setGameStatus(gameStatus);
    return this;
  }

  @Override public Game setAwayTeamId(long awayTeamId) {
    delegate.setAwayTeamId(awayTeamId);
    return this;
  }

  @Override public Game setAwayScore(int awayScore) {
    delegate.setAwayScore(awayScore);
    return this;
  }

  @Override public Game setHomeTeamId(long homeTeamId) {
    delegate.setHomeTeamId(homeTeamId);
    return this;
  }

  @Override public Game setHomeScore(int homeScore) {
    delegate.setHomeScore(homeScore);
    return this;
  }

  @Override public String toString() {
    return delegate.toString();
  }
}
