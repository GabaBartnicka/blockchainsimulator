package pl.edu.uj.gbartnicka.blockchainsimulator.hooks.impl;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Random;

@Profile({"hard"})
@RequiredArgsConstructor
@Slf4j
@Service
public class HardDataGeneratorImpl implements DataGenerator {
    private final BlockchainService blockchainService;
    private final TransactionService transactionService;
    private final Blockchain blockchain;
    private final TransactionPool transactionPool;
    private final Wallet wallet;

    @PostConstruct
    public void init() {
        log.info("Hard profile, generating test data.....");

        Fairy fairy = Fairy.create();
        Random rand = new Random();

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 3; i++) {
                createTransaction(fairy, rand);
            }
            final var validTransactions = transactionPool.validTransactions();
            var rewardTransaction = new RewardTransaction(wallet, blockchain.getWallet());
            validTransactions.add(rewardTransaction);
        }

        blockchain.replaceForce(new Blockchain(5, 3000L));
    }

    private void createTransaction(@NotNull Fairy fairy, @NotNull Random rand) {
        Person person = fairy.person();
        var pubAddress = new PublicAddress(person.getFullName());

        var t = transactionService
                .createAndBroadcastTransaction(pubAddress, BigDecimal.valueOf(rand.nextFloat() * 0.001));
        log.debug(t.toPrettyJson());
    }
}
