package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionEvent;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewTransactionPublisher implements ApplicationListener<NewTransactionEvent>, Consumer<FluxSink<NewTransactionEvent>> {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<NewTransactionEvent> queue = new LinkedBlockingQueue<>();

    @Override
    public void accept(FluxSink<NewTransactionEvent> newTransactionEventFluxSink) {
        executor.execute(() -> {
            while (true) {
                Try.run(() -> newTransactionEventFluxSink.next(queue.take()))
                   .onFailure(e -> log.error("cannot take from queue {}", e.getMessage()));
            }
        });
    }

    @Override
    public void onApplicationEvent(@NotNull NewTransactionEvent event) {
        final boolean added = queue.offer(event);
        log.info("event added={}", added);
    }
}
