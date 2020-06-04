package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

@Data
@RequiredArgsConstructor
public class BlockEnvelope {
    private final Block block;
    private final Peer sender;
}
