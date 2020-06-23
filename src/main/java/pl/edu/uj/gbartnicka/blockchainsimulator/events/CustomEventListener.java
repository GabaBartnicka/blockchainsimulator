package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.ClearTransactionPoolEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.NewBlockMinedEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.NewPeerDetectedEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnectorI;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomEventListener {

    private final PeerConnectorI peerConnectorI;

    @EventListener
    public void onNewPeerDetected(@NotNull NewPeerDetectedEvent event) {
        log.info("New peer added event - {}", event.getNewPeer());
        peerConnectorI.establishConnection(event.getNewPeer());
    }

    @EventListener
    public void onNewBlockMined(@NotNull NewBlockMinedEvent event) {
        log.info("New block mined! {}", event.getBlockEnvelope());
        peerConnectorI.sendNewBlockInfoToAll(event.getBlockEnvelope().toJson());
    }

    @EventListener
    public void onTransactionShouldBeCleared(@NotNull ClearTransactionPoolEvent event) {
        peerConnectorI.sendClearPoolToAll();
    }
}
