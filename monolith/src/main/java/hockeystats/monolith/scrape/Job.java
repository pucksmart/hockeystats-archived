package hockeystats.monolith.scrape;

import hockeystats.monolith.nhl_api.ApiResponse;
import reactor.core.publisher.Flux;
import retrofit2.Response;

public interface Job<R extends ApiResponse> {
  Flux<Response<R>> run();
}
