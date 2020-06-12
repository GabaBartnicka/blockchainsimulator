package pl.edu.uj.gbartnicka.blockchainsimulator.events.types;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

@Slf4j
@Getter
public class NewPeerDetectedEvent extends ApplicationEvent {
    private final Peer newPeer;

    public NewPeerDetectedEvent(@NotNull Peer newPeer, Object source) {
        super(source);
        this.newPeer = newPeer;
    }
}
