package pl.edu.uj.gbartnicka.blockchainsimulator.events.types;

import org.springframework.context.ApplicationEvent;

public class ClearTransactionPoolEvent extends ApplicationEvent {
    public ClearTransactionPoolEvent(Object source) {
        super(source);
    }
}
