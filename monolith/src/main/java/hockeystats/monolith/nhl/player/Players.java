package hockeystats.monolith.nhl.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class Players {
  private final PlayerRepository playerRepository;

  public Mono<Player> getForNhlId(long nhlId) {
    return playerRepository.findByNhlId(nhlId)
        .defaultIfEmpty(new Player())
        .map(WrappedPlayer::new);
  }

  public Mono<Player> save(Player player) {
    WrappedPlayer wrapped = (WrappedPlayer) player;
    log.trace(wrapped.getDelegate().toString());
    if (wrapped.getOriginalDelegateHashCode() != wrapped.getDelegate().hashCode()) {
      log.info(wrapped.getDelegate().toString());
      try {
        return playerRepository
            .save(wrapped.getDelegate())
            .map(WrappedPlayer::new);
      } catch (OptimisticLockingFailureException ignored) {
        return Mono.empty();
      }
    }
    return Mono.just(player);
  }
}
