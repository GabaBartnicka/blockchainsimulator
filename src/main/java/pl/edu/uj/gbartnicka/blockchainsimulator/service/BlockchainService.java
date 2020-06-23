package pl.edu.uj.gbartnicka.blockchainsimulator.service;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockchainData;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockchainWithoutChain;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.NewBlockMinedEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnectorI;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.RewardTransaction;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockchainService {
    private final Blockchain blockchain;
    private final ApplicationEventPublisher publisher;
    private final Peer myself;
    private final PeerConnectorI peerConnectorI;
    private final TransactionPool transactionPool;
    private final Wallet wallet;

    private final AtomicBoolean free = new AtomicBoolean(true);

    @NotNull
    public Block getLastBlock() {
        return blockchain.getLastBlock();
    }

    @PostConstruct
    public void askForBlockchain() {
        Try.run(() -> {
            final var blockchainEnvelopes = peerConnectorI.askForBlockchain();
            final var longest = blockchainEnvelopes.stream().sorted().limit(1).findFirst();
            if (longest.isEmpty()) {
                log.warn("No blockchain found!");
                return;
            }

            var blockchainCandidate = longest.get();
            blockchain.replace(blockchainCandidate.getBlockchain());
        }).onFailure(e -> log.warn("Blockchain remained the same: {}", e.getMessage()));
    }

    public void mine() {
        log.info("Starting mining process");
        final var validTransactions = transactionPool.validTransactions();

        if (validTransactions.isEmpty()) {
            log.warn("No transactions in pool!");
            return;
        }

        var rewardTransaction = new RewardTransaction(wallet, blockchain.getWallet());
        validTransactions.add(rewardTransaction);

        Block block = new Block(new BlockchainData(validTransactions));

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
            transactionPool.clear();
        });
    }

    public BlockchainEnvelope getBlockchain() {
        return new BlockchainEnvelope(blockchain, myself);
    }

    public void onNewBlock(@NotNull BlockEnvelope blockEnvelope) {
        log.info("Adding new block #{}", blockEnvelope.getBlock().getIndex());
        blockchain.forceAddNewBlock(blockEnvelope.getBlock());
    }

    @NotNull
    public BlockchainEnvelope envelope() {
        return new BlockchainEnvelope(blockchain, myself);
    }

    @NotNull
    public List<Block> blockchainPage(@NotNull Integer page, @NotNull Integer size) {
        return blockchain.getChain().stream().skip(page * size).limit(size).collect(Collectors.toList());
    }

    @NotNull
    public BlockchainWithoutChain blockchainInfo() {
        return BlockchainWithoutChain.fromBlockchain(blockchain);
    }

    @NotNull
    public Block blockByIndex(@NotNull Integer index) {
        return index >= blockchain.getSize()? blockchain.getLastBlock() : blockchain.getChain().get(index);
    }
}
