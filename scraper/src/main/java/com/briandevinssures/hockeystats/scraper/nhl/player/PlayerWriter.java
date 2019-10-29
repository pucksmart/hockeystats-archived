package com.briandevinssures.hockeystats.scraper.nhl.player;

import com.briandevinssures.hockeystats.scraper.player.Player;
import com.briandevinssures.hockeystats.scraper.player.PlayerRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerWriter implements ItemWriter<List<Player>> {

    private final PlayerRepository playerRepository;

    PlayerWriter(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void write(List<? extends List<Player>> list) throws Exception {
        list.forEach(playerRepository::saveAll);
    }
}
