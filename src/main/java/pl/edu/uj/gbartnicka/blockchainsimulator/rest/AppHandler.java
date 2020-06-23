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
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppHandler {

    private final Wallet wallet;
    private final BlockchainService blockchainService;

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
//        http://localhost:8080/blocks?page=0&size=2
        final var page = request.queryParam("page").map(Integer::valueOf).orElse(0);
        final var size = request.queryParam("size").map(Integer::valueOf).orElse(10);
        log.info("Fetching blocks: page={}, size={}", page, size);

        final var blocks = blockchainService.blockchainPage(page, size);

        log.info("Fetched {} blocks", blocks.size());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(blocks));
    }

    @NotNull
    public Mono<ServerResponse> blockByIndex(@NotNull ServerRequest request) {
        final var index = Try.of(() ->request.pathVariable("index")).map(Integer::valueOf).getOrElse(0);
        log.info("Fetching block with index: index={}", index);

        final var block = blockchainService.blockByIndex(index);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(block));
    }


    @NotNull
    public Mono<ServerResponse> blockchain(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(blockchainService.blockchainInfo()));
    }

}
