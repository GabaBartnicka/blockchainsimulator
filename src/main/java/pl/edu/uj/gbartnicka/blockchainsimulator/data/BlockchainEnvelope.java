package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainEnvelope implements Jsonable {

    private Blockchain blockchain;
    private Peer sender;

}
