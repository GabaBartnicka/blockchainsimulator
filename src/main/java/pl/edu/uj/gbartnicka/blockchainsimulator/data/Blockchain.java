package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.CollectionUtils;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Exclude;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.BlockchainWallet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.INITIAL_DIFFICULTY;
import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.MINE_RATE;

@Slf4j
@Data
public class Blockchain implements DisposableBean, Jsonable, Serializable {

    private static final long serialVersionUID = 4975597711628215690L;

    private int difficulty;
    private long mineRate;
    private List<Block> chain = new ArrayList<>();

    @Exclude
    private BlockchainWallet wallet;

    @TestOnly
    public Blockchain() {
        this(INITIAL_DIFFICULTY, MINE_RATE);
    }

    public Blockchain(@NotNull Integer difficulty, @NotNull Long mineRate) {
        this.difficulty = difficulty;
        this.mineRate = mineRate;
        chain.add(Block.genesis(difficulty));

        log.info("Created blockchain with difficulty {} and mine rate {}", difficulty, mineRate);

        this.wallet = new BlockchainWallet();
    }

    public void addBlock(@NotNull Block newBlock) {
        var prev = getLastBlock();
        log.info("Previous block {}, start mining with difficulty {}", prev, difficulty);

        newBlock.setIndex(prev.getIndex() + 1);
        newBlock.setPrevHash(prev.getHash());
        newBlock.mineBlock(difficulty);

        difficulty = adjustDifficulty(prev.getTimestamp(), newBlock.getTimestamp(), difficulty);

        log.info("New difficulty is {}", difficulty);

        chain.add(newBlock);
    }

    public Block mine(@NotNull Block newBlock) {
        addBlock(newBlock);
        return getLastBlock();
    }

    public Block getLastBlock() {
        return CollectionUtils.lastElement(chain);
    }

    public void replaceChains(List<Block> chain) {
        this.chain = chain;
    }

    public void forceAddNewBlock(@NotNull Block newBlock) {
        final var prev = getLastBlock();
        if (newBlock.getPrevHash().equals(prev.getHash())) {
            chain.add(newBlock);
            log.info("block added");
        }
    }

    @Contract(pure = true)
    private @NotNull Integer adjustDifficulty(long lastBlockTimestamp, long currentTime, Integer difficulty) {
        return Math.max(1, lastBlockTimestamp + mineRate > currentTime ? difficulty + 1 : difficulty - 1);
    }

    @Override
    public void destroy() {
        snapshot();
    }

    public boolean isEmpty() {
        return chain.size() <= 1;
    }

    public int getSize() {
        return chain.size();
    }

    public void replace(@NotNull Blockchain another) {
        log.warn("Replacing blockchain!");
        if (getSize() >= another.getSize()) {
            throw new IllegalArgumentException("Cannot replace blockchain with smaller one!");
        }

        this.difficulty = another.difficulty;
        this.mineRate = another.mineRate;
        this.chain = another.chain;
    }

    public void addTestBlock(Block block) {
        block.setPrevHash(getLastBlock().getHash());
        block.setHash(block.calculateHash());
        block.setIndex(getLastBlock().getIndex()+1);
        chain.add(block);
    }
}
