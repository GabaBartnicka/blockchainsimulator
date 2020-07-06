package pl.edu.uj.gbartnicka.blockchainsimulator.client;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Profile({"!dev"})
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

    private final DataGenerator dataGenerator;

    @ShellMethod("add new peer with given port")
    public void addPeer(@ShellOption Integer port) {
//        neighbourhoodService.addPeer(port);
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

    @ShellMethod("send ping message to all")
    public void pingAll() {
        peerConnector.pingAll();
    }

    @ShellMethod("mine new block")
    public void mine() {
        blockchainService.mine();
    }

    @ShellMethod("get all blockchain")
    public void blockchain() {
        log.info("Blockchain chains: {}", blockchain.getChain().size());
        log.info(blockchain.toPrettyJson());
    }

    @ShellMethod("get block with index")
    public void block(Integer index) {
        log.info("Block {}", blockchain.getChain().get(index).toPrettyJson());
    }

    @ShellMethod("get last block")
    public void lastBlock() {
        log.info("Block {}", blockchain.getLastBlock().toPrettyJson());
    }

    @ShellMethod("make snapshot")
    public void snapshot() {
        blockchain.snapshot();
        log.info("Done.");
    }

    @ShellMethod("synchronize blockchain")
    public void sync() {
        blockchainService.askForBlockchain();
        log.info(blockchain.toPrettyJson());
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
        int n = 3;
        Fairy fairy = Fairy.create();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            Person person = fairy.person();
            var pubAddress = new PublicAddress(person.getFullName());

            var t = transactionService.createAndBroadcastTransaction(pubAddress, BigDecimal.valueOf(rand.nextFloat()));
//            log.info(t.toPrettyJson());
        }
    }
}