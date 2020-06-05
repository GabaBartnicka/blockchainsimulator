package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class WalletTest {

    private final Logger logger = Logger.getLogger("WalletTest");

    @Test
    void testConstruct() {
        Wallet wallet = new Wallet();
        logger.info(wallet.toString());

        assertThat(wallet).hasNoNullFieldsOrProperties();
    }
}