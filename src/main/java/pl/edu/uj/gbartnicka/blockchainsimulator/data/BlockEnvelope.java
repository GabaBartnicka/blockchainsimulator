package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockEnvelope {
    private Block block;
    private Peer sender;
}