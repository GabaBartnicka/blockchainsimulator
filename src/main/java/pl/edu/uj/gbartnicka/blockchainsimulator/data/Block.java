package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;

import java.io.Serializable;
import java.util.Objects;

import static pl.edu.uj.gbartnicka.blockchainsimulator.utils.ShaSum.sha256;

@Data
@Slf4j
@NoArgsConstructor
public class Block implements Jsonable, Serializable {

    private static final long serialVersionUID = 5740538519770001546L;

    private BlockchainData data;
    private Integer index;
    private String hash;
    private String prevHash;
    private long timestamp;
    private Integer nonce;

    private Integer difficulty;

    public Block(@NotNull BlockchainData data) {
        this.data = data;

        this.nonce = 0;
        this.timestamp = DateTime.now().getMillis();
    }

    public Block(@NotNull Integer index, @NotNull String data) {
        this.index = index;
        this.data = new BlockchainData(data);

        this.nonce = 0;
        this.timestamp = DateTime.now().getMillis();
    }

    public Block(@NotNull Integer index, @NotNull String data, @NotNull Integer difficulty) {
        this.index = index;
        this.data = new BlockchainData(data);

        this.nonce = 0;
        this.timestamp = DateTime.now().getMillis();
        this.difficulty = difficulty;
    }

    @NotNull
    String calculateHash() {
        String all = index + prevHash + timestamp + data + nonce + difficulty;
        return sha256(all);
    }

    public void mineBlock(int lastBlockDifficulty) {
        difficulty = lastBlockDifficulty;
        var zeroStr = "0".repeat(lastBlockDifficulty);
        do {
            nonce++;
            timestamp = DateTime.now().getMillis();
            hash = calculateHash();
        } while (!Objects.equals(hash.substring(0, lastBlockDifficulty), zeroStr));

        log.info("Block mined! {}", hash);
    }

    static @NotNull Block genesis(int difficulty) {
        var block = new Block(0, "Block genesis", difficulty);
        block.setHash(block.calculateHash());
        return block;
    }
}
