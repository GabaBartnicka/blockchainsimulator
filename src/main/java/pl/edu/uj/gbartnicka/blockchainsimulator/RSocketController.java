package pl.edu.uj.gbartnicka.blockchainsimulator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RSocketController {

    private final Peer myself;
    private final NeighbourhoodService neighbourhoodService;

    @MessageMapping("request-response")
    SimpleMessage requestResponse(SimpleMessage request) {
        log.info("Received request-response request: {}", request);
        neighbourhoodService.addPeer(request.getPeer());
        return new SimpleMessage("pong", myself);
    }
}