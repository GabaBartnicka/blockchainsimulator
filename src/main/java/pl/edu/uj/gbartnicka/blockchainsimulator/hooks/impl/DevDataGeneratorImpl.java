package pl.edu.uj.gbartnicka.blockchainsimulator.hooks.impl;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Random;

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

        Fairy fairy = Fairy.create();
        Random rand = new Random();

        for (int j = 0; j < 3; j++) {
            int n = 3;

            for (int i = 0; i < n; i++) {
                createTransaction(fairy, rand);
            }
            blockchainService.mine();
        }

        createTransaction(fairy, rand);
    }

    private void createTransaction(Fairy fairy, Random rand) {
        Person person = fairy.person();
        var pubAddress = new PublicAddress(person.getFullName());

        var t = transactionService
                .createAndBroadcastTransaction(pubAddress, BigDecimal.valueOf(rand.nextFloat() * 0.01));
        log.info(t.toPrettyJson());
    }
}
