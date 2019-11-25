package hockeystats.monolith.nhl.season;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
class WrappedSeason extends Season {
  @JsonIgnore
  @Getter(AccessLevel.PACKAGE)
  private final Season delegate;

  @Getter(AccessLevel.PACKAGE)
  private final int originalDelegateHashCode;

  WrappedSeason(Season delegate) {
    this.delegate = delegate;
    this.originalDelegateHashCode = delegate.hashCode();
  }

  @Override public String getSeasonId() {
    return delegate.getSeasonId();
  }

  @Override public LocalDate getRegularSeasonStartDate() {
    return delegate.getRegularSeasonStartDate();
  }

  @Override public LocalDate getRegularSeasonEndDate() {
    return delegate.getRegularSeasonEndDate();
  }

  @Override public LocalDate getSeasonEndDate() {
    return delegate.getSeasonEndDate();
  }

  @Override public Integer getNumberOfGames() {
    return delegate.getNumberOfGames();
  }

  @Override public Boolean getTiesInUse() {
    return delegate.getTiesInUse();
  }

  @Override public Boolean getOlympicsParticipation() {
    return delegate.getOlympicsParticipation();
  }

  @Override public Boolean getConferencesInUse() {
    return delegate.getConferencesInUse();
  }

  @Override public Boolean getDivisionsInUse() {
    return delegate.getDivisionsInUse();
  }

  @Override public Boolean getWildCardInUse() {
    return delegate.getWildCardInUse();
  }

  @Override public Season setSeasonId(String seasonId) {
    delegate.setSeasonId(seasonId);
    return this;
  }

  @Override public Season setRegularSeasonStartDate(LocalDate regularSeasonStartDate) {
    delegate.setRegularSeasonStartDate(regularSeasonStartDate);
    return this;
  }

  @Override public Season setRegularSeasonEndDate(LocalDate regularSeasonEndDate) {
    delegate.setRegularSeasonEndDate(regularSeasonEndDate);
    return this;
  }

  @Override public Season setSeasonEndDate(LocalDate seasonEndDate) {
    delegate.setSeasonEndDate(seasonEndDate);
    return this;
  }

  @Override public Season setNumberOfGames(Integer numberOfGames) {
    delegate.setNumberOfGames(numberOfGames);
    return this;
  }

  @Override public Season setTiesInUse(Boolean tiesInUse) {
    delegate.setTiesInUse(tiesInUse);
    return this;
  }

  @Override public Season setOlympicsParticipation(Boolean olympicsParticipation) {
    delegate.setOlympicsParticipation(olympicsParticipation);
    return this;
  }

  @Override public Season setConferencesInUse(Boolean conferencesInUse) {
    delegate.setConferencesInUse(conferencesInUse);
    return this;
  }

  @Override public Season setDivisionsInUse(Boolean divisionsInUse) {
    delegate.setDivisionsInUse(divisionsInUse);
    return this;
  }

  @Override public Season setWildCardInUse(Boolean wildCardInUse) {
    delegate.setWildCardInUse(wildCardInUse);
    return this;
  }

  @Override public String toString() {
    return delegate.toString();
  }
}
