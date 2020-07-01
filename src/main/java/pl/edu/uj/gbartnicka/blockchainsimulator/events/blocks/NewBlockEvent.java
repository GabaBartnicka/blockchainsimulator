package pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;

@Getter
public class NewBlockEvent extends ApplicationEvent {

    protected transient BlockEnvelope blockEnvelope;

    public NewBlockEvent(Object source, @NotNull BlockEnvelope blockEnvelope) {
        super(source);
        this.blockEnvelope = blockEnvelope;
    }

    @NotNull
    public Block toPublish() {
        return blockEnvelope.getBlock();
    }
}
