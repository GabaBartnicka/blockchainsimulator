package pl.edu.uj.gbartnicka.blockchainsimulator.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.NewBlockMinedEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.hooks.SnapshotHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockchainService {
    private final Blockchain blockchain;
    private final SnapshotHandler saveBlockchainToFile;
    private final ApplicationEventPublisher publisher;
    private final Peer myself;
    private final PeerConnector peerConnector;

    private final AtomicBoolean free = new AtomicBoolean(true);

    @PreDestroy
    public void snapshot() {
        log.info("Destroying");
        saveBlockchainToFile.save();
    }

    @PostConstruct
    public void load() {
        saveBlockchainToFile.read().ifPresent(b -> blockchain.replaceChains(b.getChain()));
    }

    public Block getLastBlock() {
        return blockchain.getLastBlock();
    }

    public void mine(Block block) {
        if (!free.get()) {
            log.warn("Mining in progress");
            return;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            log.info("Started mining process");
            final var newBlock = blockchain.mine(block);
            free.set(true);
            publisher.publishEvent(new NewBlockMinedEvent(new BlockEnvelope(newBlock, myself), this));
        });
    }

    public BlockchainEnvelope getBlockchain() {
        return new BlockchainEnvelope(blockchain, myself);
    }

    public void synchronizeWith(Peer peer) {
        final var blockchainEnvelope = peerConnector.askForBlockchain(peer);
        if (blockchainEnvelope == null)
            return;

        if (blockchainEnvelope.getBlockchain().getChain().size() > blockchain.getChain().size())
            blockchain.replaceChains(blockchainEnvelope.getBlockchain().getChain());
    }

    public void onNewBlock(String blockEnvelope) {
        blockchain.forceAddNewBlock(new Gson().fromJson(blockEnvelope, BlockEnvelope.class).getBlock());
    }
}
