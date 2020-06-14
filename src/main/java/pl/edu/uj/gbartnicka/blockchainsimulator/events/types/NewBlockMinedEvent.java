package pl.edu.uj.gbartnicka.blockchainsimulator.events.types;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;

@Getter
public class NewBlockMinedEvent extends ApplicationEvent {
    private final BlockEnvelope blockEnvelope;

    public NewBlockMinedEvent(BlockEnvelope blockEnvelope, Object source) {
        super(source);
        this.blockEnvelope = blockEnvelope;
    }
}
