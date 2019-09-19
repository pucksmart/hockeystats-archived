package com.briandevinssures.hockeystats.scraper.player;

import com.briandevinssures.hockeystats.scraper.Strings;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NhldcPlayerScraper {
    private final RestTemplate restTemplate;
    private final PlayerRepository playerRepository;

    NhldcPlayerScraper(RestTemplateBuilder restTemplateBuilder, PlayerRepository playerRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.playerRepository = playerRepository;
    }

    public void run() {
        char[] query = new char[]{'a', 'a', 'a'};
        boolean run = true;
        while (run) {
            String url = String.format("https://suggest.svc.nhl.com/svc/suggest/v1/minplayers/%s/99999", new String(query));
            ResponseEntity<SearchSuggestionResponse> responseEntity = restTemplate.getForEntity(url, SearchSuggestionResponse.class);
            if (responseEntity.hasBody() &&
                    responseEntity.getBody().getSuggestions() != null &&
                    responseEntity.getBody().getSuggestions().size() > 0) {
                for (SearchSuggestionResponse.Fields suggestion : responseEntity.getBody().parsedSuggestions()) {
                    Optional<Player> found = playerRepository.findById(suggestion.getId());
                    found.ifPresentOrElse(p -> {
                        p.setGivenName(suggestion.getGivenName());
                        p.setFamilyName(suggestion.getFamilyName());
                        p.setPosition(suggestion.getPosition());
                        p.setHeight(suggestion.getHeight() != null ? Strings.heightToInches(suggestion.getHeight()) : null);
                        p.setWeight(suggestion.getWeight());
                        p.setBirthRegion(suggestion.getBirthRegion());
                        p.setBirthLocality(suggestion.getBirthLocality());
                        p.setBirthCountry(suggestion.getBirthCountry());
                        p.setBirthDate(suggestion.getBirthDate());
                        playerRepository.save(p);
                    }, () -> {
                        Player player = Player.builder()
                                .id(suggestion.getId())
                                .givenName(suggestion.getGivenName())
                                .familyName(suggestion.getFamilyName())
                                .position(suggestion.getPosition())
                                .height(suggestion.getHeight() != null ? Strings.heightToInches(suggestion.getHeight()) : null)
                                .weight(suggestion.getWeight())
                                .birthLocality(suggestion.getBirthLocality())
                                .birthRegion(suggestion.getBirthRegion())
                                .birthCountry(suggestion.getBirthCountry())
                                .birthDate(suggestion.getBirthDate())
                                .build();
                        playerRepository.save(player);
                    });
                }
            }

            if (query[0] == 'z' && query[1] == 'z' && query[2] == 'z') {
                run = false;
            } else {
                query[2]++;
                if (query[2] > 'z') {
                    query[2] = 'a';
                    query[1]++;
                }
                if (query[1] > 'z') {
                    query[1] = 'a';
                    query[0]++;
                }
            }
        }
    }

    @Data
    public static class SearchSuggestionResponse {
        List<String> suggestions;

        List<Fields> parsedSuggestions() {
            return suggestions.stream()
                    .map(s -> {
                        log.info(s);
                        String[] parts = s.split("\\|");
                        return Fields.builder()
                                .id(Long.parseLong(parts[0]))
                                .familyName(parts[1])
                                .givenName(parts[2])
                                .active(Integer.parseInt(parts[3]) == 1)
                                .rookie(Integer.parseInt(parts[4]) == 1)
                                .height(parts[5].isEmpty() ? null : parts[5])
                                .weight(parts[6].length() > 0 ? Integer.parseInt(parts[6]) : null)
                                .birthLocality(parts[7].isEmpty() ? null : parts[7])
                                .birthRegion(parts[8].isEmpty() ? null : parts[8])
                                .birthCountry(parts[9].isEmpty() ? null : parts[9])
                                .birthDate(LocalDate.parse(parts[10]))
                                .team(parts[11].isEmpty() ? null : parts[11])
                                .position(parts[12].isEmpty() ? null : parts[12])
                                .number(parts[13].length() > 0 ? Integer.parseInt(parts[13]) : null)
                                .urlSuffix(parts[14])
                                .build();
                    })
                    .collect(Collectors.toList());
        }

        @Data
        @Builder
        public static class Fields {
            private long id;
            private String familyName;
            private String givenName;
            private boolean active;
            private boolean rookie;
            private String height;
            private Integer weight;
            private String birthLocality;
            private String birthRegion;
            private String birthCountry;
            private LocalDate birthDate;
            private String team;
            private String position;
            private Integer number;
            private String urlSuffix;
        }
    }
}
