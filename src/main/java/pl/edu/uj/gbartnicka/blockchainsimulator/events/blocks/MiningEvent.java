package pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.rest.MiningStatus;

@Getter
public class MiningEvent extends ApplicationEvent {

    private final MiningStatus status;

    public MiningEvent(Object source, @NotNull Boolean busy) {
        super(source);
        this.status = new MiningStatus(getTimestamp(), busy);
    }
}
