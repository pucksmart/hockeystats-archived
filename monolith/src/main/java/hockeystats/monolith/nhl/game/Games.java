package hockeystats.monolith.nhl.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class Games {
  private final GameRepository gameRepository;

  public Mono<Game> getForId(long id) {
    return gameRepository.findById(id)
        .defaultIfEmpty(new Game())
        .map(WrappedGame::new);
  }

  public Mono<Game> save(Game game) {
    WrappedGame wrapped = (WrappedGame) game;
    if (wrapped.getOriginalDelegateHashCode() != wrapped.getDelegate().hashCode()) {
      try {
        return gameRepository
            .save(wrapped.getDelegate())
            .map(WrappedGame::new);
      } catch (OptimisticLockingFailureException ignored) {
        return Mono.empty();
      }
    }
    return Mono.just(game);
  }
}
