package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.Security;

import static org.assertj.core.api.Assertions.assertThat;

class KeysTest {

    @Test
    void generateKeys() {
        final var keyPair = Keys.generateKeys();
        assertThat(keyPair).isNotNull();
    }

    @Test
    void testDecodeEncoded() {
        Security.addProvider(new BouncyCastleProvider());
        final var generated = Keys.generateKeys();
        final var originalPubKey = generated.getPublic();
        final var encoded = originalPubKey.getEncoded();

        final var decoded = Keys.decodePublicKey(encoded);

        assertThat(originalPubKey).isEqualTo(decoded);
    }
}