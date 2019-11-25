package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl_api.ApiResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retrofit2.Response;

@RequiredArgsConstructor
abstract class ApiConsumerTask<R extends ApiResponse> {
  final ResourceRepository resourceRepository;

  abstract Flux<Response<R>> run();

  final Flux<Response<R>> runWithFilter() {
    return run()
        .filterWhen(this::apiResponseUpdated);
  }

  private Mono<Boolean> apiResponseUpdated(Response<? extends ApiResponse> apiResponse) {
    if (apiResponse == null || apiResponse.body() == null) {
      return Mono.just(false);
    }
    String url = apiResponse.raw().request().url().toString();
    String hash = apiResponse.body().getHash();

    return resourceRepository.findById(url)
        .defaultIfEmpty(new Resource())
        .map(r -> {
          int existingHashCode = r.hashCode();
          r.setUrl(url)
              .setMd5(hash);
          if (existingHashCode == r.hashCode()) {
            return false;
          } else {
            resourceRepository.save(r).subscribe();
            return true;
          }
        });
  }
}
