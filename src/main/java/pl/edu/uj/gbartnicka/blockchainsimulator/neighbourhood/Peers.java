package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;

import java.util.Set;

@Data
@AllArgsConstructor
public class Peers implements Jsonable {
    private Set<Peer> peers;
}
