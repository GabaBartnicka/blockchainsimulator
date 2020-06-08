package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

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

    public static Optional<KeyPair> recover(byte[] encodedPriv, PublicKey publicKey) {
        return Try.of(() -> {
            // A KeyFactory is used to convert encoded keys to their actual Java classes
            KeyFactory ecKeyFac = KeyFactory.getInstance("EC", "BC");

            // Now do a round-trip for a private key,
            // now take the encoded value and recreate the private key
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPriv);
            PrivateKey privateKey = ecKeyFac.generatePrivate(pkcs8EncodedKeySpec);

            // And a round trip for the public key as well.
            byte[] encodedPub = publicKey.getEncoded();
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPub);
            PublicKey publicKey2 = ecKeyFac.generatePublic(x509EncodedKeySpec);
            return Optional.of(new KeyPair(publicKey2, privateKey));
        })
                .onFailure(e -> log.info("Cannot recover keys: {}", e.getMessage()))
                .getOrElse(Optional::empty);
    }
}
