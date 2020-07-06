package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import io.vavr.control.Try;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionEvent;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class NewTransactionPublisher implements ApplicationListener<NewTransactionEvent>, Consumer<FluxSink<NewTransactionEvent>> {

    private final Executor executor;
    private final BlockingQueue<NewTransactionEvent> queue = new LinkedBlockingQueue<>();

    @Override
    public void accept(FluxSink<NewTransactionEvent> newTransactionEventFluxSink) {
        AtomicBoolean cancel = new AtomicBoolean(false);

        newTransactionEventFluxSink.onCancel(() -> {
                    cancel.set(true);
                    log.info("newTransactionEventFluxSink canceled");
                }
        );

        log.info("accept");
        executor.execute(() -> {
            while (!cancel.get()) {
                Try.run(() -> newTransactionEventFluxSink.next(queue.take()))
                        .onFailure(e -> log.error("cannot take from queue {}", e.getMessage()))
                        .onSuccess(b -> log.info("New transaction taken"));
            }
        });
    }

    @Override
    public void onApplicationEvent(@NotNull NewTransactionEvent event) {
        final boolean added = queue.offer(event);
        log.info("event added={}", added);
    }
}
