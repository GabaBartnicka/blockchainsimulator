package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewBlockPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewTransactionPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.NewBlockEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequestMapping("events")
public class EventsController {

    private final Flux<NewBlockEvent> newBlockEventFlux;
    private Flux<NewTransactionEvent> newTransactionEventFlux;

    public EventsController(@NotNull NewBlockPublisher newBlockPublisher,
                            @NotNull NewTransactionPublisher newTransactionPublisher) {
        this.newBlockEventFlux = Flux.create(newBlockPublisher).share();
        this.newTransactionEventFlux = Flux.create(newTransactionPublisher).log().share();

        this.newTransactionEventFlux
                .doOnCancel(() ->
                        this.newTransactionEventFlux = Flux.create(newTransactionPublisher).log().share());
    }

    @GetMapping(path = "/block", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Block> onNewBlockSubscribe() {
        log.info("Subscribing to new block event");
        return newBlockEventFlux.map(NewBlockEvent::toPublish);
    }

    @GetMapping(path = "/transaction", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> onNewBlockTransaction() {
        log.info("Subscribing to new transaction event");
        return newTransactionEventFlux.map(NewTransactionEvent::toPublish);
    }
}
