package pl.edu.uj.gbartnicka.blockchainsimulator.hooks.impl;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;

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

    @PostConstruct
    public void init() {
        log.info("init");
        if (blockchainService.getBlockchain().getBlockchain().getSize() >= 1) {
            log.info("initializing data...");

            int n = 3;
            Fairy fairy = Fairy.create();
            Random rand = new Random();

            for (int i = 0; i < n; i++) {
                Person person = fairy.person();
                var pubAddress = new PublicAddress(person.getFullName());

                var t = transactionService.createAndBroadcastTransaction(pubAddress, BigDecimal.valueOf(rand.nextFloat() * 0.01));
                log.info(t.toPrettyJson());
            }
            blockchainService.mine();
        }
    }
}
