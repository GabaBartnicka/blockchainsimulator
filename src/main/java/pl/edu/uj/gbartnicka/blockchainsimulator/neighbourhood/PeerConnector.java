package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeerConnector {
    private final RSocketRequester.Builder rsocketRequesterBuilder;
    private final Map<Peer, Mono<RSocketRequester>> connections = new HashMap<>();
    private final Peer myself;

    public void establishConnection(Peer peer) {
        log.info("Establishing connection to {}", peer);

        Try.run(() -> {
            var conn = rsocketRequesterBuilder.connectTcp("localhost", peer.getPort());
            connections.put(peer, conn);
            ping(peer);
        })
                .onFailure(e -> log.error("Cannot establish contection to peer {}, reason is: {}", peer, e.getMessage()))
                .onSuccess(b -> log.info("Connection to peer {} successfully established", peer));
    }

    public void ping(Peer peer) {
        log.info("Ping to {}", peer);
        var data = new SimpleMessage("ping", myself);
        if (!connections.containsKey(peer)) {
            log.warn("No connection to peer {}, trying to establish", peer);
            establishConnection(peer);
        }

        Try.run(() -> {
            final RSocketRequester rSocketRequester = connections.get(peer).block();
            var response = rSocketRequester.route("request-response").data(data).retrieveMono(SimpleMessage.class).block();
            log.info("Response: {}", response);
        }).onFailure(e -> log.error("Cannot send ping to peer {} - {}", peer, e.getMessage()));
    }
}
