package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;

import java.util.Objects;

import static pl.edu.uj.gbartnicka.blockchainsimulator.utils.ShaSum.sha256;

@Data
@Slf4j
public class Block implements Jsonable {

    private final BlockchainData data;
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
    private String calculateHash() {
        String all = index + prevHash + timestamp + data + nonce + difficulty;
        return sha256(all);
    }

    public void mineBlock(int lastBlockDifficulty) {
        difficulty = lastBlockDifficulty;
        var zeroStr = "0".repeat(Math.max(0, lastBlockDifficulty));
        do {
            nonce++;
            timestamp = DateTime.now().getMillis();
            hash = calculateHash();
        } while (!Objects.equals(hash.substring(0, lastBlockDifficulty), zeroStr));

        log.info("Block mined! {}", hash);
    }
}
