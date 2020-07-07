package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionCreatedEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.TransactionEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.rest.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionService implements Pageable<Transaction> {
    private final PeerConnector peerConnector;
    private final TransactionPool transactionPool;
    private final Wallet wallet;
    private final Peer myself;
    private final ApplicationEventPublisher publisher;

    public void handleIncomingTransaction(@NotNull TransactionEnvelope envelope) {
        log.info("Received {}", envelope);
    }

    @NotNull
    public Transaction createAndBroadcastTransaction(@NotNull PublicAddress recipient, @NotNull BigDecimal amount) {
        if (amount.signum() < 1) {
            throw new IllegalArgumentException("Balance has to be grater than 0");
        }
        final var transaction = wallet.createTransaction(recipient, amount, transactionPool);
        log.debug("Publishing events about new transaction! {}", transaction.getId());
        publisher.publishEvent(new NewTransactionCreatedEvent(this, new TransactionEnvelope(myself, transaction)));
        peerConnector.sendNewTransactionToAll(new TransactionEnvelope(myself, transaction));
        return transaction;
    }

    @Override
    public List<Transaction> ranged(@NotNull Integer from, @NotNull Integer to) {
        var collect = transactionPool.getTransactions().stream().sorted().skip(from).limit(to + 1L - from)
                                              .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    public int numberOfTransactions() {
        return transactionPool.getTransactions().size();
    }
}
