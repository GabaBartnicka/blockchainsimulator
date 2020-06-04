package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewPeerDetectedEventListener implements ApplicationListener<NewPeerDetectedEvent> {

    private final PeerConnector peerConnector;

    @Override
    public void onApplicationEvent(NewPeerDetectedEvent event) {
        log.info("New peer added event - {}", event.getNewPeer());
        peerConnector.establishConnection(event.getNewPeer());
    }
}
