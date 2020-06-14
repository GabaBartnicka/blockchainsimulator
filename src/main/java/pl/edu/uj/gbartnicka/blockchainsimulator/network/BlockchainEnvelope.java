package pl.edu.uj.gbartnicka.blockchainsimulator.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Envelope;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainEnvelope implements Envelope, Comparable<BlockchainEnvelope> {

    private Blockchain blockchain;
    private Peer sender;

    @Override
    public int compareTo(@NotNull BlockchainEnvelope o) {
        return blockchain.getSize() - o.getBlockchain().getSize();
    }
}
