package pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks;

import lombok.Getter;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;

@Getter
public class NewBlockMinedEvent extends NewBlockEvent {

    public NewBlockMinedEvent(BlockEnvelope blockEnvelope, Object source) {
        super(source, blockEnvelope);
    }
}
