package pl.edu.uj.gbartnicka.blockchainsimulator.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Envelope;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainRequest implements Envelope {
    private Peer sender;
    private long timestamp;
}
