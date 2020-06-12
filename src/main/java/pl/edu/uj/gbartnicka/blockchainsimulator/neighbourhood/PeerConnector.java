package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public BlockchainEnvelope askForBlockchain(@NotNull Peer peer) {
        var data = new SimpleMessage("ping", myself);
        if (!connections.containsKey(peer)) {
            log.warn("No connection to peer {}, trying to establish", peer);
            establishConnection(peer);
        }

        return Try.of(() -> {
            final RSocketRequester rSocketRequester = connections.get(peer).block();
            var response = rSocketRequester.route("request-blockchain").data(data).retrieveMono(String.class).block();
            log.info("Response: {}", response);
            return new Gson().fromJson(response, BlockchainEnvelope.class);
        }).onFailure(e -> log.error("Cannot send ping to peer {} - {}", peer, e.getMessage())).getOrNull();
    }

    public void sendNewBlockInfoToAll(@NotNull String block) {
        sendToAll(block, "new-block");
    }

    public void sendNewTransactionToAll(@NotNull String transaction) {
        sendToAll(transaction, "new-transaction");
    }

    @NotNull
    private List<String> sendToAll(@NotNull String data, @NotNull String route) {
        return connections.keySet().stream().map(peer -> sendToOne(peer, data, route)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    private Optional<String> sendToOne(@NotNull Peer peer, @NotNull String data, @NotNull String route) {
        log.info("Sending data via {} to {}", route, peer);
        return Try.of(() -> {
            final RSocketRequester rSocketRequester = connections.get(peer).block();
            var response = rSocketRequester.route(route).data(data).retrieveMono(String.class).block();
            log.debug("Response: {}", response);
            return Optional.ofNullable(response);

        }).onFailure(e -> log.error("Cannot send data via route {} to peer {} because of: {}", peer, route, e.getMessage()))
                .getOrElse(Optional::empty);
    }
}
