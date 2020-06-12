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
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class ShellController {

    private final NeighbourhoodService neighbourhoodService;
    private final PeerConnector peerConnector;
    private final Blockchain blockchain;
    private final BlockchainService blockchainService;

    private final Wallet wallet;
    private final TransactionPool transactionPool;
    private final TransactionService transactionService;

    @ShellMethod("add new peer with given port")
    public void addPeer(@ShellOption Integer port) {
        neighbourhoodService.addPeer(port);
    }

    @ShellMethod("list all peers")
    public void listPeers() {
        neighbourhoodService.peers().stream().map(Peer::toString).reduce((s, s2) -> s + "\n" + s2)
                .ifPresentOrElse(p -> log.info("Peers: \n{}", p), () -> log.info("No peers available!"));
    }

    @ShellMethod("send ping message")
    public void ping(@ShellOption String peer) {
        Optional<Peer> p = neighbourhoodService.peer(peer);
        p.ifPresentOrElse(peerConnector::ping, () -> log.warn("Peer {} not found :-(", peer));
    }

    @ShellMethod("mine new block")
    public void mine(@ShellOption Integer index, @ShellOption String data) {
        blockchainService.mine(new Block(index, data));
    }

    @ShellMethod("get all blockchain")
    public void blockchain() {
        log.info("Blockchain info: {}", blockchain);
        log.info(blockchain.toPrettyJson());
    }

    @ShellMethod("get block with index")
    public void block(Integer index) {
        log.info("Block {}", blockchain.getChain().get(index).toPrettyJson());
    }

    @ShellMethod("make snapshot")
    public void snapshot() {
        blockchain.snapshot();
        log.info("Done.");
    }

    @ShellMethod("synchronize blockchain")
    public void sync(@ShellOption String peer) {
        Optional<Peer> p = neighbourhoodService.peer(peer);
        p.ifPresentOrElse(blockchainService::synchronizeWith, () -> log.warn("Peer {} not found :-(", peer));
    }

    @ShellMethod("prints transaction pool")
    public void transactions() {
        log.info(transactionPool.toPrettyJson());
    }

    @ShellMethod("prints wallet info")
    public void wallet() {
        log.info(wallet.toPrettyJson());
    }

    @ShellMethod("creates new transaction")
    public void transfer(@ShellOption String recipient, @ShellOption Long amount) {
        final var transaction = transactionService.createAndBroadcastTransaction(new PublicAddress(recipient), BigDecimal.valueOf(amount));
        log.info(transaction.toPrettyJson());
    }

    @ShellMethod("creates new example transaction")
    public void et() {
        final var transaction = transactionService.createAndBroadcastTransaction(new PublicAddress("asdf"), BigDecimal.ONE);
        log.info(transaction.toPrettyJson());
    }
}