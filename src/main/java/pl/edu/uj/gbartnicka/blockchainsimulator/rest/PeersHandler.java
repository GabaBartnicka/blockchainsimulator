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
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class PeersHandler {

    private final NeighbourhoodService neighbourhoodService;
    private final PeerConnector peerConnector;

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
        return peer.map(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                           .body(BodyInserters.fromValue(peer)))
                   .orElseGet(() -> ServerResponse.notFound().build());
    }

    public @NotNull Mono<ServerResponse> addNewPeer(@NotNull ServerRequest request) {
        log.info("Adding new peer");
        var message = request.bodyToMono(Peer.class).map(newPeer -> {
            log.info("Adding new peer: {}", newPeer);
            if (neighbourhoodService.addPeerIfDoesNotExist(newPeer)) {
                return "ok";
            }
            return "Peer " + newPeer + "already exists";
        });
        log.info("{}", message);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(message, String.class);
    }

    public @NotNull Mono<ServerResponse> deletePeer(@NotNull ServerRequest request) {
        log.info("Adding new peer");
        final var name = request.pathVariable("name");
        if (StringUtils.isBlank(name)) {
            return ServerResponse.notFound().build();
        }
        neighbourhoodService.delete(name);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue("deleted"));
    }

    public @NotNull Mono<ServerResponse> ping(@NotNull ServerRequest request) {
        final var name = request.pathVariable("name");
        log.info("Pinging peer: {}", name);
        if (StringUtils.isBlank(name)) {
            log.warn("No name found!");
            return ServerResponse.notFound().build();
        }
        var pinged = peerConnector.ping(name);
        return pinged ? ServerResponse.ok().build() :
                ServerResponse.ok()
                              .contentType(MediaType.APPLICATION_JSON)
                              .body(BodyInserters.fromValue("Peer " + name + " is unavailable"));

    }
}
