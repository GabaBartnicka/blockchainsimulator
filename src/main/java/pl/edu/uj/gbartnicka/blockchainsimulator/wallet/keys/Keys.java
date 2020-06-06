package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Slf4j
public class Keys {

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

    public static boolean verify(@NotNull byte[] signature, @NotNull String plainData, @NotNull PublicKey pubKey) {
        return Try.of(() -> {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
            ecdsaVerify.initVerify(pubKey);
            ecdsaVerify.update(plainData.getBytes(StandardCharsets.UTF_8));
            boolean result = ecdsaVerify.verify(signature);
            log.info("Verification={}", result);
            return result;
        })
                .onFailure(e -> log.error("Cannot sign data! {}", e.getMessage()))
                .getOrElseThrow(e -> new CannotSignDataException(e.getMessage(), e));
    }
}
