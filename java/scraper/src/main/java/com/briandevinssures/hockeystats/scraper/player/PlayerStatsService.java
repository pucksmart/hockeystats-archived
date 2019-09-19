package com.briandevinssures.hockeystats.scraper.player;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PlayerStatsService {
    private static final String REGULAR_SEASON_CAREER_URL = "https://statsapi.web.nhl.com/api/v1/people/{id}/stats?stats=careerPlayoffs&expand=stats.team";

    private final RestTemplate restTemplate;
    private final PlayerNhlRegularSeasonCareerStatsRepository playerNhlRegularSeasonCareerStatsRepository;

    PlayerStatsService(RestTemplateBuilder restTemplateBuilder, PlayerNhlRegularSeasonCareerStatsRepository playerNhlRegularSeasonCareerStatsRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.playerNhlRegularSeasonCareerStatsRepository = playerNhlRegularSeasonCareerStatsRepository;
    }

    void fetchNhlRegularSeasonCareer(long playerId) {
        ResponseEntity<StatsApiResponse> responseEntity = restTemplate.getForEntity(REGULAR_SEASON_CAREER_URL, StatsApiResponse.class, playerId);
        if (!responseEntity.hasBody()) {
            return;
        }
        StatsApiResponse response = responseEntity.getBody();

        Optional<PlayerNhlRegularSeasonCareerStats> existing = playerNhlRegularSeasonCareerStatsRepository.findById(playerId);
        if (existing.isPresent()) {
            existing.get().gamesPlayed(response.getStats().get(0).getSplits().get(0).getStat().getGamesPlayed());
        }
    }
}
