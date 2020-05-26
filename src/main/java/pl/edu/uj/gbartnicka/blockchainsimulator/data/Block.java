package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.Objects;

import static pl.edu.uj.gbartnicka.blockchainsimulator.utils.ShaSum.sha256;

@Data
@Slf4j
public class Block {

    private String hash;
    private String prevHash;

    private final Integer index;
    private final String data;
    private final long timestamp;
    private Integer nonce;

    public Block(@NotNull Integer index, @NotNull String data) {
        this.index = index;
        this.data = data;

        this.nonce = 0;
        this.timestamp = DateTime.now().getMillis();
    }

    @NotNull
    private String calculateHash() {
        String all = index + prevHash + timestamp + data + nonce;
        return sha256(all);
    }

    public void mineBlock(int difficulty) {
        var zeroStr = "0".repeat(Math.max(0, difficulty));
        do {
            nonce++;
            hash = calculateHash();
        } while (!Objects.equals(hash.substring(0, difficulty), zeroStr));

        log.info("Block mined! {}", hash);
    }
}
