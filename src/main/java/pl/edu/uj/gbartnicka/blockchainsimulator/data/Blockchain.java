package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.INITIAL_DIFFICULTY;
import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.MINE_RATE;

@Slf4j
@Data
public class Blockchain {

    private int difficulty;
    private long mineRate = MINE_RATE;
    private List<Block> chain = new ArrayList<>();

    public Blockchain() {
        this.difficulty = INITIAL_DIFFICULTY;
        chain.add(new Block(0, "Block genesis", difficulty));

        log.info("Created blockchain with difficulty {} and mine rate {}", difficulty, mineRate);
    }

    public void addBlock(@NotNull Block newBlock) {
        var prev = getLastBlock();
        log.info("Previous block {}, start mining with difficulty {}", prev, difficulty);

        newBlock.setPrevHash(prev.getHash());
        newBlock.mineBlock(prev.getDifficulty());

        difficulty = adjustDifficulty(prev.getTimestamp(), newBlock.getTimestamp());
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
    private @NotNull Integer adjustDifficulty(long lastBlockTimestamp, long currentTime) {
        return lastBlockTimestamp + mineRate > currentTime ? difficulty + 1 : difficulty - 1;
    }
}
