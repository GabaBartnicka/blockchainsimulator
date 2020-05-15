package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class NeighbourhoodService {

    private final Set<Peer> peers = new HashSet<>();

    public void addPeer(Integer port) {
        addPeer(new Peer(port));
    }

    public void addPeer(Peer peer) {
        boolean add = peers.add(peer);
        if (add) {
            log.info("Added new peer {}", peer);
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
