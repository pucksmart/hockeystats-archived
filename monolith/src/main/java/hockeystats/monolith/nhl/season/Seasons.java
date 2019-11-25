package hockeystats.monolith.nhl.season;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class Seasons {
  private final SeasonRepository seasonRepository;

  public Mono<Season> getById(String id) {
    return seasonRepository.findById(id)
        .defaultIfEmpty(new Season())
        .map(WrappedSeason::new);
  }

  public Flux<Season> getAll() {
    return seasonRepository.findAll()
        .map(WrappedSeason::new);
  }

  public Mono<Season> save(Season season) {
    WrappedSeason wrapped = (WrappedSeason) season;
    log.trace(wrapped.getDelegate().toString());
    if (wrapped.getOriginalDelegateHashCode() != wrapped.getDelegate().hashCode()) {
      log.info(wrapped.getDelegate().toString());
      try {
        return seasonRepository
            .save(wrapped.getDelegate())
            .map(WrappedSeason::new);
      } catch (OptimisticLockingFailureException ignored) {
        return Mono.empty();
      }
    }
    return Mono.just(season);
  }
}
