package hockeystats.monolith.nhl_api.suggest;

import hockeystats.monolith.nhl_api.ApiResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class SuggestPlayers extends ApiResponse {
  List<SuggestPlayer> suggestions;

  public void setSuggestions(List<String> strings) {
    this.suggestions = strings.stream()
        .map(s -> {
          String[] parts = s.split("\\|");
          return new SuggestPlayer()
              .setId(Long.parseLong(parts[0]))
              .setFamilyName(parts[1])
              .setGivenName(parts[2])
              .setActive(Integer.parseInt(parts[3]) == 1)
              .setRookie(Integer.parseInt(parts[4]) == 1)
              .setHeight(parts[5].isEmpty() ? null : parts[5])
              .setWeight(parts[6].isEmpty() ? null : Integer.parseInt(parts[6]))
              .setBirthLocality(parts[7].isEmpty() ? null : parts[7])
              .setBirthRegion(parts[8].isEmpty() ? null : parts[8])
              .setBirthCountry(parts[9].isEmpty() ? null : parts[9])
              .setBirthDate(LocalDate.parse(parts[10]))
              .setTeam(parts[11].isEmpty() ? null : parts[11])
              .setPosition(parts[12].isEmpty() ? null : parts[12])
              .setNumber(parts[13].isEmpty() ? null : Integer.parseInt(parts[13]))
              .setUrlSuffix(parts[14]);
        })
        .collect(Collectors.toList());
  }
}
