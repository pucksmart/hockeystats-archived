package com.briandevinssures.hockeystats.scraper.player;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class PlayerService {

    private final PlayerRepository playerRepository;

    PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    Optional<Player> getPlayerForNhlId(long nhlId) {
        return playerRepository.findByNhlId(nhlId);
    }
}
