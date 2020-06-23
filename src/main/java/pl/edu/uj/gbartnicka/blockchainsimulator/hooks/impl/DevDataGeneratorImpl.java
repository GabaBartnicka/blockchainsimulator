package pl.edu.uj.gbartnicka.blockchainsimulator.hooks.impl;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataLoader;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.SnapshotCreator;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;

import javax.annotation.PostConstruct;

@Profile("dev")
@RequiredArgsConstructor
@Slf4j
@Service
public class DevDataGeneratorImpl implements DataGenerator {
    private final BlockchainService blockchainService;
    private final TransactionService transactionService;
    private final Blockchain blockchain;
    private final TransactionPool transactionPool;

    @PostConstruct
    public void init() {
        log.info("Dev profile, generating test data");

        Try.run(() -> {
            DataLoader.readTransactionPool(SnapshotCreator
                    .pathDirectoryPeerBasedProject(TransactionPool.class.getSimpleName().toLowerCase() + "_db.txt"))
                      .ifPresent(pool -> transactionPool.setTransactions(pool.getTransactions()));
            DataLoader.readBlockchain(SnapshotCreator
                    .pathDirectoryPeerBasedProject(Blockchain.class.getSimpleName().toLowerCase() + "_db.txt"))
                      .ifPresent(blockchain::replace);

        }).onFailure(e->log.error("message: {}", e.getMessage()));


//        log.info("init");
//        if (blockchainService.getBlockchain().getBlockchain().getSize() <= 1) {
//            log.info("initializing data...");
//
//            int n = 3;
//            Fairy fairy = Fairy.create();
//            Random rand = new Random();
//
//            for (int i = 0; i < n; i++) {
//                Person person = fairy.person();
//                var pubAddress = new PublicAddress(person.getFullName());
//
//                var t = transactionService.createAndBroadcastTransaction(pubAddress, BigDecimal.valueOf(rand.nextFloat() * 0.01));
//                log.info(t.toPrettyJson());
//            }
//            blockchainService.mine();
//        }
    }
}
