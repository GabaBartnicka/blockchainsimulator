package pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.TransactionEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;

@Getter
public class NewTransactionEvent extends ApplicationEvent {

    protected transient TransactionEnvelope transactionEnvelope;

    public NewTransactionEvent(Object source, @NotNull TransactionEnvelope transactionEnvelope) {
        super(source);
        this.transactionEnvelope = transactionEnvelope;
    }

    public Transaction toPublish() {
        return transactionEnvelope.getTransaction();
    }
}
