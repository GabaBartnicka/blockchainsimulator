package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Block {

    private String hash;
    private String prevHash;
    private Integer index;
    private long created = Instant.now().getEpochSecond();

}
