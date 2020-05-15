package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeerConnector {
    private final RSocketRequester.Builder rsocketRequesterBuilder;

    private Map<Peer, RSocketRequester> connections = new HashMap<>();

    private final Peer myself;

    public void establishConnection(Peer peer) {
        log.info("Establishing connection to {}", peer);
        var conn = rsocketRequesterBuilder.connectTcp("localhost", peer.getPort()).block();
        connections.put(peer, conn);
    }

    public void ping(Peer peer) {
        log.info("Ping to {}", peer);

        var data = new SimpleMessage("ping", myself);
        final RSocketRequester rSocketRequester = connections.get(peer);
        var response = rSocketRequester.route("request-response").data(data).retrieveMono(SimpleMessage.class).block();

        log.info("Response: {}", response);
    }

    public void pingAll() {
        log.info("Sending one request. Waiting for one response...");
        var data = new SimpleMessage("ping", myself);
        var responses = connections.values().stream().map(requestor ->
                requestor.route("request-response").data(data).retrieveMono(SimpleMessage.class).block()
        ).peek(r -> log.info("Response: {}", r)).collect(Collectors.toList());
    }
}
