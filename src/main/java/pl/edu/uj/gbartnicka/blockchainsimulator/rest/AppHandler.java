package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppHandler {

    private final PeerConnector peerConnector;
    private final Blockchain blockchain;
    private final BlockchainService blockchainService;

    private final Wallet wallet;
    private final TransactionPool transactionPool;
    private final TransactionService transactionService;

    private final DataGenerator dataGenerator;

    @NotNull
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromValue("Hello, Spring!"));
    }

    @NotNull
    public Mono<ServerResponse> wallet(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(wallet));
    }

    @NotNull
    public Mono<ServerResponse> balance(ServerRequest request) { // TODO
        var value = new BigDecimal(10);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(value));
    }

    @NotNull
    public Mono<ServerResponse> blocks(@NotNull ServerRequest request) {
        final var from = request.queryParam("from").map(Integer::valueOf)
                                .orElse(Math.max(0, blockchain.getSize() - 10));
        final var to = request.queryParam("to").map(Integer::valueOf).orElse(blockchain.getSize());
        log.info("Fetching blocks: from={}, to={}", from, to);

        final var blocks = blockchainService.ranged(from, to);

        log.info("Fetched {} blocks", blocks.size());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(blocks));
    }

    @NotNull
    public Mono<ServerResponse> blockByIndex(@NotNull ServerRequest request) {
        final var index = Try.of(() -> request.pathVariable("index")).map(Integer::valueOf).getOrElse(0);
        log.info("Fetching block with index: index={}", index);

        final var block = blockchainService.blockByIndex(index);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(block));
    }

    @NotNull
    public Mono<ServerResponse> blockchain(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(blockchainService.blockchainInfo()));
    }

    @NotNull
    public Mono<ServerResponse> mine(ServerRequest request) {
        log.warn("Not implemented yet");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue("Mining process has started"));
    }

}
