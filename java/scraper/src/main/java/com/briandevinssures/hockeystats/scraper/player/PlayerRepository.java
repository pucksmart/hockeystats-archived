package com.briandevinssures.hockeystats.scraper.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface PlayerRepository extends JpaRepository<Player, Long> {
}
