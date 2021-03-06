package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.NewBlockEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionEvent;
import reactor.core.publisher.FluxSink;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewBlockPublisher implements ApplicationListener<NewBlockEvent>, Consumer<FluxSink<NewBlockEvent>> {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<NewBlockEvent> queue = new LinkedBlockingQueue<>();

    @Override
    public void accept(@NotNull FluxSink<NewBlockEvent> fluxSink) {
        AtomicBoolean cancel = new AtomicBoolean(false);
        fluxSink.onCancel(() -> {
                    cancel.set(true);
                    log.info("newBlockEventFluxSink cancelled");
                }
        );

        executor.execute(() -> {
            AtomicReference<NewBlockEvent> newBlockEvent = new AtomicReference<>();
            while (!cancel.get()) {
                var eventOpt = Try.of(queue::take)
                                  .onFailure(e -> log.error("cannot take new block event from queue {}", e.getMessage()))
                                  .onSuccess(b -> log.debug("Emitting event"))
                                  .map(Optional::of)
                                  .getOrElse(Optional::empty);
                eventOpt.ifPresent(ev -> {
                    fluxSink.next(ev);
                    newBlockEvent.set(ev);
                });
            }
            onApplicationEvent(newBlockEvent.get());
            log.debug("exiting loop");
        });
    }

    @Override
    public void onApplicationEvent(@NotNull NewBlockEvent event) {
        final boolean added = queue.offer(event);
        log.debug("event added={}", added);
    }
}
