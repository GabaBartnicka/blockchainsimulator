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
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.MiningEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks.NewBlockMinedEvent;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.rest.Pageable;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.RewardTransaction;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockchainService implements Pageable<Block> {
    private final Blockchain blockchain;
    private final ApplicationEventPublisher publisher;
    private final Peer myself;
    private final PeerConnector peerConnector;
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
            final var blockchainEnvelopes = peerConnector.askForBlockchain();
            final var longest = blockchainEnvelopes.stream().sorted().limit(1).findFirst();
            if (longest.isEmpty()) {
                log.warn("No blockchain found!");
                return;
            }

            var blockchainCandidate = longest.get();
            blockchain.replace(blockchainCandidate.getBlockchain());
        }).onFailure(e -> log.warn("Blockchain remained the same: {}", e.getMessage()));
    }

    public boolean mine() {
        log.info("Starting mining process");
        final var validTransactions = transactionPool.validTransactions();

        if (validTransactions.isEmpty()) {
            log.warn("No transactions in pool!");
            return false;
        }

        var rewardTransaction = new RewardTransaction(wallet, blockchain.getWallet());
        validTransactions.add(rewardTransaction);

        Block block = new Block(new BlockchainData(validTransactions));

        if (!free.get()) {
            log.warn("Mining in progress");
            return false;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            log.info("Started mining process with {} transactions", validTransactions.size());
            free.set(false);

            publisher.publishEvent(new MiningEvent(this, true));
            final var newBlock = blockchain.mine(block);
            publisher.publishEvent(new MiningEvent(this, false));

            free.set(true);
            publisher.publishEvent(new NewBlockMinedEvent(new BlockEnvelope(newBlock, myself), this));
            wallet.updateBalance(validTransactions);
            transactionPool.clear(validTransactions);

        });

        return true;
    }

    public boolean miningInProgress() {
        log.info("free: {}", free.get());
        return !free.get();
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
    @Override
    public List<Block> ranged(@NotNull Integer from, @NotNull Integer to) {
        if(from < 0 || to < 0)
            return Collections.emptyList();

        final List<Block> collect = blockchain.getChain().stream()
                                              .skip(from)
                                              .limit(to + 1L - from)
                                              .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    @NotNull
    public BlockchainWithoutChain blockchainInfo() {
        log.info("current blockchain size = {}", blockchain.getSize());
        return BlockchainWithoutChain.fromBlockchain(blockchain, wallet.getBalance());
    }

    @NotNull
    public Block blockByIndex(@NotNull Integer index) {
        return index >= blockchain.getSize() ? blockchain.getLastBlock() : blockchain.getChain().get(index);
    }
}
