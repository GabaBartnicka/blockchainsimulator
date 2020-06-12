package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.NewPeerDetectedEvent;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class NeighbourhoodService {

    private final Set<Peer> peers = new HashSet<>();

    private final ApplicationEventPublisher publisher;
    private final Peer myself;
    @Value("${app.peers}")
    private String peersProperty;

    @PostConstruct
    public void initialize() {
        Try.of(() -> peers.addAll(
                Stream.of(peersProperty.split(","))
                        .map(Peer::resolve)
                        .filter(p -> !p.equals(myself))
                        .peek(p -> publisher.publishEvent(new NewPeerDetectedEvent(p, this)))
                        .collect(Collectors.toSet())))
                .onFailure(e -> log.error("Cannot collect peers: {}", e.getMessage(), e))
                .onSuccess(b -> log.info("Peers collected successfully, current peers number: {}", peers.size()));
    }

    public void addPeer(@NotNull Integer port) {
        addPeer(new Peer(port));
    }

    public void addPeer(@NotNull Peer peer) {
        boolean add = peers.add(peer);
        if (add) {
            log.info("Added new peer {}", peer);
            publisher.publishEvent(new NewPeerDetectedEvent(peer, this));
        }
    }

    public Set<Peer> peers() {
        return new HashSet<>(peers);
    }

    public Optional<Peer> peer(Integer port) {
        return peers.stream().filter(p -> p.getPort().equals(port)).findFirst();
    }

    public Optional<Peer> peer(String name) {
        return peers.stream().filter(p -> p.getName().equals(name)).findFirst();
    }
}
