package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

public interface Envelope extends Jsonable {

    @NotNull
    Peer getSender();
}
