package com.briandevinssures.hockeystats.scraper.nhl.player;

import com.briandevinssures.hockeystats.scraper.Strings;
import com.briandevinssures.hockeystats.scraper.nhl_api.suggest.PlayerSuggestions;
import com.briandevinssures.hockeystats.scraper.player.Player;
import com.briandevinssures.hockeystats.scraper.player.PlayerRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlayerSuggestionsProcessor implements ItemProcessor<PlayerSuggestions, List<Player>> {

    private final PlayerRepository playerRepository;

    PlayerSuggestionsProcessor(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> process(PlayerSuggestions playerSuggestions) throws Exception {
        if (playerSuggestions.getSuggestions().isEmpty()) {
            return null;
        }
        return playerSuggestions.getSuggestions().stream().map(ps -> {
            Optional<Player> existing = playerRepository.findByNhlId(ps.getId());

            Player.PlayerBuilder playerBuilder = existing.map(Player::toBuilder).orElse(Player.builder());

            return playerBuilder
                .nhlId(ps.getId())
                .givenName(ps.getGivenName())
                .familyName(ps.getFamilyName())
                .position(ps.getPosition())
                .height(Strings.heightToInches(ps.getHeight()))
                .weight(ps.getWeight())
                .birthLocality(ps.getBirthLocality())
                .birthRegion(ps.getBirthRegion())
                .birthCountry(ps.getBirthCountry())
                .birthDate(ps.getBirthDate())
                .build();
        }).collect(Collectors.toList());
    }
}
