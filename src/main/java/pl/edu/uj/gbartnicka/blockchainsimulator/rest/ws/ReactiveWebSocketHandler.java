package pl.edu.uj.gbartnicka.blockchainsimulator.rest.ws;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewTransactionPublisher;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.transactions.NewTransactionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final Flux<NewTransactionEvent> newTransactionEventFlux;

    public ReactiveWebSocketHandler(NewTransactionPublisher newTransactionPublisher) {
        this.newTransactionEventFlux = Flux.create(newTransactionPublisher);
    }

    @Override
    public Mono<Void> handle(@NotNull WebSocketSession webSocketSession) {

        return webSocketSession.send(newTransactionEventFlux.map(e -> e.toPublish().toJson())
                .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .log());
    }
}