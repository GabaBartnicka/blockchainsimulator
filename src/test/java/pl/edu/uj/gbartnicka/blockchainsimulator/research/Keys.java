package pl.edu.uj.gbartnicka.blockchainsimulator.research;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;


public class Keys {

    @Test
    void keyGenerationRSA() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println(privateKey.getFormat());
        PublicKey publicKey = keyPair.getPublic();
        System.out.println(publicKey.getFormat() + ", key: \n" + publicKey.toString());
    }

    @Test
    void keyGenerationECC() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, InvalidKeySpecException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp521r1"), new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println(privateKey.getFormat());
        PublicKey publicKey = keyPair.getPublic();
        System.out.println(publicKey.getFormat());

        // A KeyFactory is used to convert encoded keys to their actual Java classes
        KeyFactory ecKeyFac = KeyFactory.getInstance("EC", "BC");

        // Now do a round-trip for a private key,
        byte[] encodedPriv = privateKey.getEncoded();
        // now take the encoded value and recreate the private key
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPriv);
        PrivateKey privateKey2 = ecKeyFac.generatePrivate(pkcs8EncodedKeySpec);

        // And a round trip for the public key as well.
        byte[] encodedPub = publicKey.getEncoded();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPub);
        PublicKey publicKey2 = ecKeyFac.generatePublic(x509EncodedKeySpec);
        System.out.println(publicKey2);
    }

    private KeyPair generateKeys() throws InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp256k1"), new SecureRandom());
//        keyPairGenerator.initialize(new ECGenParameterSpec("secp521r1"), new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    @Test
    void sign() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        var plaintext = "Text to be signed";

        final var pair = generateKeys();

        // ECDSA (Elliptic Curve Digital Signature Algorithm)
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");

        // do sign
        ecdsaSign.initSign(pair.getPrivate());
        ecdsaSign.update(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] signature = ecdsaSign.sign();

        // verify
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaVerify.initVerify(pair.getPublic());
        ecdsaVerify.update(plaintext.getBytes(StandardCharsets.UTF_8));
        boolean result = ecdsaVerify.verify(signature);
        assertThat(result).isTrue();
    }
}
