package pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions;

import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.TransactionEnvelope;

public class NewTransactionCreatedEvent extends NewTransactionEvent{
    public NewTransactionCreatedEvent(Object source, @NotNull TransactionEnvelope transactionEnvelope) {
        super(source, transactionEnvelope);
    }
}
