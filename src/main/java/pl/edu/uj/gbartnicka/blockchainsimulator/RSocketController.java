package pl.edu.uj.gbartnicka.blockchainsimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;

@Slf4j
@Controller
public class RSocketController {

    @MessageMapping("request-response")
    SimpleMessage requestResponse(SimpleMessage request) {
        log.info("Received request-response request: {}", request);
        // create a single Message and return it
        return new SimpleMessage("pong");
    }
}