package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

@Slf4j
public class KeysGenerator {

    public static KeyPair generateKeys() {
        Security.addProvider(new BouncyCastleProvider());
        return Try.of(() -> {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            keyPairGenerator.initialize(new ECGenParameterSpec("secp256k1"), new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        })
                .onSuccess(b -> log.info("Keys generated successfully"))
                .onFailure(e -> log.error("Cannot generate keys: {}", e.getMessage()))
                .getOrElseThrow(e -> new CannotGenerateKeyException("Generating keys problem" + e.getMessage(), e));
    }
}
