package pl.edu.uj.gbartnicka.blockchainsimulator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnectorI;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.TransactionEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final PeerConnectorI peerConnectorI;
    private final TransactionPool transactionPool;
    private final Wallet wallet;
    private final Peer myself;

    public void handleIncomingTransaction(@NotNull TransactionEnvelope envelope) {
        log.info("Received {}", envelope);
    }

    @NotNull
    public Transaction createAndBroadcastTransaction(@NotNull PublicAddress recipient, @NotNull BigDecimal amount) {
        final var transaction = wallet.createTransaction(recipient, amount, transactionPool);
        peerConnectorI.sendNewTransactionToAll(new TransactionEnvelope(myself, transaction));
        return transaction;
    }
}
