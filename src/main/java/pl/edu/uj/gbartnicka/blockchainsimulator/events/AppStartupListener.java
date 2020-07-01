package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;


@Component
@Slf4j
@RequiredArgsConstructor
public class AppStartupListener {
    private final NeighbourhoodService neighbourhoodService;
    private final PeerConnector peerConnector;
    private final Blockchain blockchain;
    private final BlockchainService blockchainService;

    private final Wallet wallet;
    private final TransactionPool transactionPool;
    private final TransactionService transactionService;

    private final DataGenerator dataGenerator;

    @EventListener(classes = { ContextStartedEvent.class, ApplicationReadyEvent.class})
    public void onAppStarted() {
        log.info("App has started");
    }
}
