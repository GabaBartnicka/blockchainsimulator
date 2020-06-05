package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewBlockMinedEventListener implements ApplicationListener<NewBlockMinedEvent> {

    private final PeerConnector peerConnector;

    @Override
    public void onApplicationEvent(NewBlockMinedEvent event) {
        log.info("New block mined! {}", event.getBlockEnvelope());
        peerConnector.newBlockMined(new Gson().toJson(event.getBlockEnvelope()));
    }
}
