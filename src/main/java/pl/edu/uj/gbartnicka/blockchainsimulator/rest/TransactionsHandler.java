package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionService;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionsHandler {
    private final TransactionService transactionService;

    @NotNull
    public Mono<ServerResponse> transactionPool(@NotNull ServerRequest request) {
        var transactionSize = transactionService.numberOfTransactions();

        var fromQuery = request.queryParam("from");
        var toQuery = request.queryParam("to");

        final var from = fromQuery.map(Integer::valueOf).orElse(Math.max(transactionSize - 10, 0));
        final var to = toQuery.map(Integer::valueOf).orElse(transactionSize);
        log.info("Fetching transactions: from={}, to={}", from, to);

        final var transactions = transactionService.ranged(from, to);

        log.info("Fetched {} transactions", transactions.size());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(transactions));
    }

    public @NotNull Mono<ServerResponse> performTransaction(@NotNull ServerRequest request) {
        /**
         * {
         *     "publicAddress": {
         *         "name": "asd",
         *         "label": "as"
         *     },
         *     "amount": -1
         * }
         */

        final Mono<TransactionResponse> monoMap =
                request.bodyToMono(TransactionRequest.class)
                       .map(t -> TransactionResponse.of(transactionService.createAndBroadcastTransaction(t.getPublicAddress(), t.getAmount())));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(monoMap, Transaction.class);
    }

}
