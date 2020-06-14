package pl.edu.uj.gbartnicka.blockchainsimulator.events.types;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;

@Getter
public class NewBlockReceived extends ApplicationEvent {
    private final BlockEnvelope block;

    public NewBlockReceived(Object source, @NotNull BlockEnvelope block) {
        super(source);
        this.block = block;
    }
}
