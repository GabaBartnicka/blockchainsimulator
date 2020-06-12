package pl.edu.uj.gbartnicka.blockchainsimulator.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final PeerConnector peerConnector;
    private final TransactionPool transactionPool;
    private final Wallet wallet;

    public void handleIncomingTransaction(@NotNull String envelope) {
        log.info("Received {}", envelope);

        var transaction = new Gson().fromJson(envelope, Transaction.class);

        log.info("Object {}", transaction);
    }

    @NotNull
    public Transaction createAndBroadcastTransaction(@NotNull PublicAddress recipient, @NotNull BigDecimal amount) {
        final var transaction = wallet.createTransaction(recipient, amount, transactionPool);
        peerConnector.sendNewTransactionToAll(transaction.toJson());
        return transaction;
    }
}
