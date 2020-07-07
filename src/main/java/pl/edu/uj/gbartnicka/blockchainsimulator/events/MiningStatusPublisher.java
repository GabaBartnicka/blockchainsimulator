package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.MiningEvent;
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
public class MiningStatusPublisher implements ApplicationListener<MiningEvent>, Consumer<FluxSink<MiningEvent>> {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<MiningEvent> queue = new LinkedBlockingQueue<>();

    @Override
    public void accept(@NotNull FluxSink<MiningEvent> fluxSink) {
        AtomicBoolean cancel = new AtomicBoolean(false);
        fluxSink.onCancel(() -> {
                    cancel.set(true);
                    log.info("miningEventFluxSink cancelled");
                }
        );

        executor.execute(() -> {
            AtomicReference<MiningEvent> newBlockEvent = new AtomicReference<>();
            while (!cancel.get()) {
                var eventOpt = Try.of(queue::take)
                                  .onFailure(e -> log.error("cannot take mining event from queue {}", e.getMessage()))
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
    public void onApplicationEvent(@NotNull MiningEvent event) {
        final boolean added = queue.offer(event);
        log.debug("event added={}", added);
    }
}
