package pl.edu.uj.gbartnicka.blockchainsimulator.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.hooks.SnapshotHandler;

import java.util.Optional;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class ShellController {

    private final NeighbourhoodService neighbourhoodService;
    private final PeerConnector peerConnector;
    private final Blockchain blockchain;
    private final SnapshotHandler saveBlockchainToFile;

    private final BlockchainService blockchainService;

    @ShellMethod("add new peer with given port")
    public void addPeer(@ShellOption Integer port) {
        neighbourhoodService.addPeer(port);
    }

    @ShellMethod("list all peers")
    public void listPeers() {
        var all = neighbourhoodService.peers().stream().map(Peer::toString).reduce((s, s2) -> s + "\n" + s2).get();
        log.info("Peers: \n{}", all);
    }

    @ShellMethod("send ping message")
    public void ping(@ShellOption String peer) {
        Optional<Peer> p = neighbourhoodService.peer(peer);
        p.ifPresentOrElse(peerConnector::ping, () -> log.warn("Peer {} not found :-(", peer));
    }

    @ShellMethod("mine new block")
    public void mine(@ShellOption Integer index, @ShellOption String data) {
        blockchain.addBlock(new Block(index, data));
    }

    @ShellMethod("get all blockchain")
    public void blockchain() {
        log.info("Blockchain info: {}", blockchain);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var json = gson.toJson(blockchain);
        log.info(json);
    }

    @ShellMethod("get block with index")
    public void block(Integer index) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var json = gson.toJson(blockchain.getChain().get(index));
        log.info("Block {}", json);
    }

    @ShellMethod("make snapshot")
    public void snapshot() {
        saveBlockchainToFile.save();
        log.info("Done.");
    }
}