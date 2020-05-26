package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Blockchain {

    private int difficulty;
    private List<Block> chain = new ArrayList<>();

    public Blockchain() {
        this.difficulty = 2;
        chain.add(new Block(0, "Block genesis"));

        log.info("Created blockchain with difficulty {}", difficulty);
    }

    public void addBlock(@NotNull Block newBlock) {
        var prev = getLastBlock();
        log.info("Previous block {}", prev);

        newBlock.setPrevHash(prev.getHash());
        newBlock.mineBlock(difficulty);

        chain.add(newBlock);
    }

    private Block getLastBlock() {
        return CollectionUtils.lastElement(chain);
    }
}
