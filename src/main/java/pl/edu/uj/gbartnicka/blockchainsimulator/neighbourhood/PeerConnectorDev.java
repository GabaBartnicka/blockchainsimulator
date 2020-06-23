package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.TransactionEnvelope;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class PeerConnectorDev implements PeerConnectorI {

    @PostConstruct
    public void init() {
        log.info("using dummy connector");
    }

    @Override
    public void establishConnection(Peer peer) {

    }

    @Override
    public void ping(Peer peer) {

    }

    @Override
    public List<BlockchainEnvelope> askForBlockchain() {
        return null;
    }

    @Override
    public BlockchainEnvelope askForBlockchain(@NotNull Peer peer) {
        return null;
    }

    @Override
    public void sendClearPoolToAll() {

    }

    @Override
    public void sendNewBlockInfoToAll(@NotNull String block) {

    }

    @Override
    public void sendNewTransactionToAll(@NotNull TransactionEnvelope transaction) {

    }

    @Override
    public void pingAll() {

    }
}
