package pl.edu.uj.gbartnicka.blockchainsimulator.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;

import java.util.Optional;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class ShellController {

    private final NeighbourhoodService neighbourhoodService;
    private final PeerConnector peerConnector;
    private final Blockchain blockchain;

    @ShellMethod("Test shell")
    public void greet(@ShellOption String text) {
        log.info("Hi, {}", text);
    }

    @ShellMethod("Adding peer")
    public void addPeer(@ShellOption Integer port) {
        neighbourhoodService.addPeer(port);
    }

    @ShellMethod("Peers")
    public void listPeers() {
        var all = neighbourhoodService.peers().stream().map(Peer::toString).reduce((s, s2) -> s + "\n" + s2).get();
        log.info("Peers: \n{}", all);
    }

    @ShellMethod("Ping")
    public void ping(@ShellOption String peer) {
        Optional<Peer> p = neighbourhoodService.peer(peer);
        p.ifPresentOrElse(peerConnector::ping, () -> log.warn("Peer {} not found :-(", peer));
    }

    @ShellMethod("Mine")
    public void mine(@ShellOption Integer index, @ShellOption String data) {
        blockchain.addBlock(new Block(index, data));
    }
}