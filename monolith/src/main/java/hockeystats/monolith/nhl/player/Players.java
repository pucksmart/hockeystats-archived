package hockeystats.monolith.nhl.player;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class Players {
  private final PlayerRepository playerRepository;

  public Mono<Player> getForNhlId(long nhlId) {
    return playerRepository.findByNhlId(nhlId)
        .defaultIfEmpty(new Player())
        .map(WrappedPlayer::new);
  }

  public Mono<Player> save(Player player) {
    WrappedPlayer wrapped = (WrappedPlayer) player;
    if (wrapped.getOriginalDelegateHashCode() != wrapped.getDelegate().hashCode()) {
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
