package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.junit.jupiter.api.Test;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

        var recipient = new PublicAddress(Keys.generateKeys().getPublic());
        var pool = new TransactionPool();
        wallet.createTransaction(recipient, BigDecimal.ONE, pool);
        assertThat(pool.getTransactions()).hasSize(1);

        wallet.createTransaction(recipient, BigDecimal.valueOf(2L), pool);
        assertThat(pool.getTransactions()).hasSize(1);
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        var wallet = new Wallet();
        var fileOutputStream = new FileOutputStream("yourfile.txt");
        var objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(wallet);
        objectOutputStream.flush();
        objectOutputStream.close();

        var fileInputStream = new FileInputStream("yourfile.txt");
        var objectInputStream = new ObjectInputStream(fileInputStream);
        var wallet2 = (Wallet) objectInputStream.readObject();
        objectInputStream.close();
        assertThat(wallet).isEqualTo(wallet2);

        var keyPair = wallet.getKeyPair();
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

        assertThat(wallet.getKeyPair().getPublic()).isEqualTo(publicKey2);
        assertThat(wallet.getKeyPair().getPrivate()).isEqualTo(privateKey2);
//        assertThat(wallet.getKeyPair()).isEqualTo(new KeyPair(publicKey2, privateKey2));

        assertThat(Paths.get("yourfile.txt").toFile().delete()).isTrue();
    }
}