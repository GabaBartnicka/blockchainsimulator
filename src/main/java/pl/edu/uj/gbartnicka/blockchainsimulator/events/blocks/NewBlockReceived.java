package pl.edu.uj.gbartnicka.blockchainsimulator.events.blocks;

import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;

public class NewBlockReceived extends NewBlockEvent {

    public NewBlockReceived(Object source, @NotNull BlockEnvelope block) {
        super(source, block);
    }
}
