package hockeystats.monolith.nhl.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
class WrappedPlayer extends Player {
  @JsonIgnore
  @Getter(AccessLevel.PACKAGE)
  private final Player delegate;

  @Getter(AccessLevel.PACKAGE)
  private final int originalDelegateHashCode;

  WrappedPlayer(Player delegate) {
    this.delegate = delegate;
    this.originalDelegateHashCode = delegate.hashCode();
  }

  @Override public String getId() {
    return delegate.getId();
  }

  @Override public long getNhlId() {
    return delegate.getNhlId();
  }

  @Override public String getGivenName() {
    return delegate.getGivenName();
  }

  @Override public String getFamilyName() {
    return delegate.getFamilyName();
  }

  @Override public Integer getNumber() {
    return delegate.getNumber();
  }

  @Override public String getPosition() {
    return delegate.getPosition();
  }

  @Override public String getBirthLocality() {
    return delegate.getBirthLocality();
  }

  @Override public String getBirthRegion() {
    return delegate.getBirthRegion();
  }

  @Override public String getBirthCountry() {
    return delegate.getBirthCountry();
  }

  @Override public LocalDate getBirthDate() {
    return delegate.getBirthDate();
  }

  @Override public Integer getHeight() {
    return delegate.getHeight();
  }

  @Override public Integer getWeight() {
    return delegate.getWeight();
  }

  @Override public Player setNhlId(long nhlId) {
    delegate.setNhlId(nhlId);
    return this;
  }

  @Override public Player setGivenName(String givenName) {
    delegate.setGivenName(givenName);
    return this;
  }

  @Override public Player setFamilyName(String familyName) {
    delegate.setFamilyName(familyName);
    return this;
  }

  @Override public Player setNumber(Integer number) {
    delegate.setNumber(number);
    return this;
  }

  @Override public Player setPosition(String position) {
    delegate.setPosition(position);
    return this;
  }

  @Override public Player setBirthLocality(String birthLocality) {
    delegate.setBirthLocality(birthLocality);
    return this;
  }

  @Override public Player setBirthRegion(String birthRegion) {
    delegate.setBirthRegion(birthRegion);
    return this;
  }

  @Override public Player setBirthCountry(String birthCountry) {
    delegate.setBirthCountry(birthCountry);
    return this;
  }

  @Override public Player setBirthDate(LocalDate birthDate) {
    delegate.setBirthDate(birthDate);
    return this;
  }

  @Override public Player setHeight(Integer height) {
    delegate.setHeight(height);
    return this;
  }

  @Override public Player setWeight(Integer weight) {
    delegate.setWeight(weight);
    return this;
  }

  @Override public String toString() {
    return delegate.toString();
  }
}
