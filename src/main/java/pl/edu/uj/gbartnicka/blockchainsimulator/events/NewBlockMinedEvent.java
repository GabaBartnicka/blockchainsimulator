package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockEnvelope;

@Getter
public class NewBlockMinedEvent extends ApplicationEvent {
    private final BlockEnvelope blockEnvelope;

    public NewBlockMinedEvent(BlockEnvelope blockEnvelope, Object source) {
        super(source);
        this.blockEnvelope = blockEnvelope;
    }
}
