package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.junit.jupiter.api.Test;
import pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues;

import static org.assertj.core.api.Assertions.assertThat;

class RewardTransactionTest {

    @Test
    void testCreate() {
        var wallet = new Wallet();
        var blockchainWallet = new Wallet();
        var t = new RewardTransaction(wallet, blockchainWallet);

        assertThat(t.isValid()).isTrue();
        assertThat(t.allOutputsAmount()).isEqualTo(DefaultValues.MINING_REWARD);
    }
}