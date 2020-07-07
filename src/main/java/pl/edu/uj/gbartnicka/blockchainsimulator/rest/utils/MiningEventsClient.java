package pl.edu.uj.gbartnicka.blockchainsimulator.rest.utils;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalTime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.MiningEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.rest.MiningStatus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@Slf4j
public class MiningEventsClient {

    public static void consumeServerSentEvent() {
        WebClient client = WebClient.create("http://localhost:8001/");
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        var eventStream = client.get()
                                .uri("/events/mining")
                                .retrieve()
                                .bodyToFlux(MiningStatus.class);

        eventStream.subscribe(
                content -> log.info("Time: {} - event: id [{}]",
                        LocalTime.now(), content.getBusy()),
                error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!"));
    }

//    public static void main(String[] args) throws InterruptedException {
//        final var countDownLatch = new CountDownLatch(1);
//
//        Executors.newSingleThreadExecutor().execute(MiningEventsClient::consumeServerSentEvent);
//
//        countDownLatch.await();
//    }
}
