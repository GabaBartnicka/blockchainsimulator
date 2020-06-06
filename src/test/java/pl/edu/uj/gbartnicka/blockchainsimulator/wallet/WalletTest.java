package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class WalletTest {

    private final Logger logger = Logger.getLogger("WalletTest");

    @Test
    void test() {
        Wallet wallet = new Wallet();
        logger.info(wallet.toString());

        assertThat(wallet).hasNoNullFieldsOrProperties();

        wallet.sign("sth");

        var pool = new TransactionPool();
        wallet.createTransaction("asdf", BigDecimal.ONE, pool);
        assertThat(pool.getTransactions()).hasSize(1);

        wallet.createTransaction("asdf", BigDecimal.valueOf(2L), pool);
        assertThat(pool.getTransactions()).hasSize(1);
    }
}