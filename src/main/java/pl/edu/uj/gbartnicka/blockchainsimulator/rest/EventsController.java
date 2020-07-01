package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewBlockPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.NewBlockEvent;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequestMapping("events")
public class EventsController {

    private final Flux<NewBlockEvent> newBlockEventFlux;

    public EventsController(@NotNull NewBlockPublisher newBlockPublisher) {
        this.newBlockEventFlux = Flux.create(newBlockPublisher).share();
    }

    @GetMapping(path = "/block", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> test() {
        log.info("Subscribing to new block event");
        return newBlockEventFlux.map(NewBlockEvent::mapToJson);
    }

}
