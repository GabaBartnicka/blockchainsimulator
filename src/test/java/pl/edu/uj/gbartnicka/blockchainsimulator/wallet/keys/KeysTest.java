package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeysTest {

    @Test
    void generateKeys() {
        final var keyPair = Keys.generateKeys();
        assertThat(keyPair).isNotNull();
    }
}