package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.MiningStatusPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewBlockPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewTransactionPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.MiningEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.NewBlockEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequestMapping("events")
public class EventsController {

    private Flux<NewBlockEvent> newBlockEventFlux;
    private Flux<NewTransactionEvent> newTransactionEventFlux;
    private Flux<MiningEvent> miningEventFlux;

    public EventsController(@NotNull NewBlockPublisher newBlockPublisher,
                            @NotNull NewTransactionPublisher newTransactionPublisher,
                            @NotNull MiningStatusPublisher miningStatusPublisher) {
        this.newBlockEventFlux = Flux.create(newBlockPublisher).log().share();
        this.newTransactionEventFlux = Flux.create(newTransactionPublisher).log().share();
        this.miningEventFlux = Flux.create(miningStatusPublisher).log().share();

        this.newBlockEventFlux.doOnCancel(() -> this.newBlockEventFlux = Flux.create(newBlockPublisher).log().share());
        this.newTransactionEventFlux.doOnCancel(() -> this.newTransactionEventFlux = Flux.create(newTransactionPublisher).log().share());
        this.miningEventFlux.doOnCancel(() -> this.miningEventFlux = Flux.create(miningStatusPublisher).log().share());
    }

    @GetMapping(path = "/v0/block", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Block> onNewBlockSubscribe() {
        log.info("Subscribing to new block event");
        return newBlockEventFlux.map(NewBlockEvent::toPublish);
    }

    @GetMapping(path = "/v0/transaction", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> onNewBlockTransaction() {
        log.info("Subscribing to new transaction event");
        return newTransactionEventFlux.map(NewTransactionEvent::toPublish);
    }

    @GetMapping(path = "/v0/mining", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MiningStatus> onMine() {
        log.info("Subscribing to mining event");
        return miningEventFlux.map(MiningEvent::getStatus);
    }
}
