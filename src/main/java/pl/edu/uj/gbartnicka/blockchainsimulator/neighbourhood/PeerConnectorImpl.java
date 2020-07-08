package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.*;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeerConnectorImpl implements PeerConnector {
    private final RSocketRequester.Builder rsocketRequesterBuilder;
    private final Map<Peer, Mono<RSocketRequester>> connections = new HashMap<>();
    private final Peer myself;
    private final NeighbourhoodService neighbourhoodService;

    @PostConstruct
    public void init() {
        neighbourhoodService.peers().forEach(this::establishConnection);
    }

    @Override
    public void establishConnection(Peer peer) {
        log.info("Establishing connection to {}", peer);

        Try.run(() -> {
            var conn = rsocketRequesterBuilder.connectTcp(myself.getHost(), peer.getPort());
            connections.put(peer, conn);
            ping(peer);
        })
           .onFailure(e -> log.error("Cannot establish connection to peer {}, reason is: {}", peer, e.getMessage()))
           .onSuccess(b -> log.info("Connection to peer {} successfully established", peer));
    }

    @Override
    public void ping(Peer peer) {
        log.info("Ping to {}", peer);
        var data = new SimpleMessage("ping", myself);
        if (!connections.containsKey(peer)) {
            log.warn("No connection to peer {}, trying to establish", peer);
            establishConnection(peer);
        }

        Try.run(() -> {
            final RSocketRequester rSocketRequester = connections.get(peer).block();
            var response = rSocketRequester.route("request-response").data(data).retrieveMono(SimpleMessage.class)
                                           .block();
            log.info("Response: {}", response);
        }).onFailure(e -> log.error("Cannot send ping to peer {} - {}", peer, e.getMessage()));
    }

    @Override
    public List<BlockchainEnvelope> askForBlockchain() {
        final var request = new BlockchainRequest(myself, DateTime.now().getMillis());
        return sendRequestToAll(request, "request-blockchain-sync", BlockchainEnvelope.class);
    }

    @Override
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

    @Override
    public void sendClearPoolToAll() {
        sendToAll("clear pool command", "clear-pool");
    }

    @Override
    public void sendNewBlockInfoToAll(@NotNull String block) {
        sendToAll(block, "new-block");
    }

    @Override
    public void sendNewTransactionToAll(@NotNull TransactionEnvelope transaction) {
        log.info("Sending transaction info to others");
        sendRequestToAll(transaction, "new-transaction", CommonResponse.class);
    }

    @NotNull
    private List<String> sendToAll(@NotNull String data, @NotNull String route) {
        log.info("Sending message to all peers via {} route", route);
        return connections.keySet().stream().map(peer -> sendToOne(peer, data, route)).filter(Optional::isPresent)
                          .map(Optional::get).collect(Collectors.toList());
    }

    @NotNull
    private <S, T> List<T> sendRequestToAll(@NotNull S data, @NotNull String route, @NotNull Class<T> responseClass) {
        return connections.keySet().stream().map(peer -> sendRequestToOne(peer, data, responseClass, route))
                          .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Optional<String> sendToOne(@NotNull Peer peer, @NotNull String data, @NotNull String route) {
        log.info("Sending data via {} to {}", route, peer);
        return Try.of(() -> {
            final RSocketRequester rSocketRequester = connections.get(peer).block();
            var response = rSocketRequester.route(route).data(data).retrieveMono(String.class).block();
            log.debug("Response: {}", response);
            return Optional.ofNullable(response);

        }).onFailure(e -> log
                .error("Cannot send data via route {} to peer {} because of: {}", peer, route, e.getMessage()))
                  .getOrElse(Optional::empty);
    }

    @Nullable
    private <T, S> T sendRequestToOne(@NotNull Peer peer, @NotNull S data, @NotNull Class<T> responseClass, @NotNull String route) {
        log.info("Sending data via {} to {}", route, peer);
        return Try.of(() -> {
            final RSocketRequester rSocketRequester = connections.get(peer).block();
            var response = rSocketRequester.route(route).data(data).retrieveMono(responseClass).block();
            log.debug("Response: {}", response);
            return response;

        }).onFailure(e -> log
                .error("Cannot send data via route {} to peer {} because of: {}", peer, route, e.getMessage()))
                  .getOrNull();
    }

    @Override
    public void pingAll() {
        sendToAll("ping from " + myself.toString(), "ping-pong");
    }
}
