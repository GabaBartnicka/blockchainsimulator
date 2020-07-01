package pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions;

import org.springframework.context.ApplicationEvent;

public class ClearTransactionPoolEvent extends ApplicationEvent {
    public ClearTransactionPoolEvent(Object source) {
        super(source);
    }
}
