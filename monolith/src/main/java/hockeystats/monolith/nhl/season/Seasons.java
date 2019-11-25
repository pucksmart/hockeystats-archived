package hockeystats.monolith.nhl.season;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
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
    if (wrapped.getOriginalDelegateHashCode() != wrapped.getDelegate().hashCode()) {
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
