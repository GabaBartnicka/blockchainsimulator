package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class PeersHandler {

    private final NeighbourhoodService neighbourhoodService;

    @NotNull
    public Mono<ServerResponse> all(@NotNull ServerRequest request) {
        final var peers = neighbourhoodService.peers();
        log.info("Found {} peers", peers.size());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(peers));
    }

    @NotNull
    public Mono<ServerResponse> byName(@NotNull ServerRequest request) {
        final var name = request.pathVariable("name");
        if (StringUtils.isBlank(name)) {
            return ServerResponse.notFound().build();
        }
        final var peer = neighbourhoodService.findByName(name);
        return peer.map(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(peer))).orElseGet(() -> ServerResponse.notFound().build());
    }

}
