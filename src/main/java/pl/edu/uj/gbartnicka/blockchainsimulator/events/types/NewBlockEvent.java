package pl.edu.uj.gbartnicka.blockchainsimulator.events.types;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;

@Getter
public class NewBlockEvent extends ApplicationEvent {

    protected BlockEnvelope blockEnvelope;

    public NewBlockEvent(Object source, @NotNull BlockEnvelope blockEnvelope) {
        super(source);
        this.blockEnvelope = blockEnvelope;
    }

    public String mapToJson() {
        return blockEnvelope.getBlock().toJson();
    }
}
