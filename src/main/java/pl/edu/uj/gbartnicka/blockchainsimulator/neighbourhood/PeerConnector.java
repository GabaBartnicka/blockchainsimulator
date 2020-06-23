package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.TransactionEnvelope;

import java.util.List;

public interface PeerConnector {
    void establishConnection(Peer peer);

    void ping(Peer peer);

    List<BlockchainEnvelope> askForBlockchain();

    BlockchainEnvelope askForBlockchain(@NotNull Peer peer);

    void sendClearPoolToAll();

    void sendNewBlockInfoToAll(@NotNull String block);

    void sendNewTransactionToAll(@NotNull TransactionEnvelope transaction);

    void pingAll();
}
