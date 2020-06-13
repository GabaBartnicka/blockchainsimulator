package pl.edu.uj.gbartnicka.blockchainsimulator.hooks;

import com.devskiller.jfairy.Fairy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peers;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SnapshotCreatorTest {

    private final String host = "localhost";
    private final String port = "808080";
    private final String dir = host + "_" + port;

    private final File dirFile = Paths.get("src", "main", "resources", "db", dir).toFile();
    private final File blockchainFile = Paths.get("src", "main", "resources", "db", dir, "blockchain_db.txt").toFile();
    private final File walletFile = Paths.get("src", "main", "resources", "db", dir, "wallet_db.txt").toFile();
    private final File transactionPoolFile = Paths.get("src", "main", "resources", "db", dir, "transactionpool_db.txt").toFile();
    private final File peersFile = Paths.get("src", "main", "resources", "db", dir, "peers_db.txt").toFile();

    @Mock
    private Peer me;

    @BeforeEach
    void setUp() {
        when(me.getHost()).thenReturn("localhost");
        when(me.getPort()).thenReturn(Integer.valueOf(port));
        SnapshotCreator.loadPeer(me);
    }

    @Test
    void saveBlockchain() {
        var blockchain = new Blockchain();
        blockchain.setDifficulty(1);
        blockchain.addBlock(new Block(1, "asdf"));

        SnapshotCreator.save(blockchain.toJson(), blockchain.filename());

        assertThat(blockchainFile.exists()).isTrue();

        final var readBlockchain = DataLoader.readBlockchain();

        assertThat(readBlockchain).isPresent().get().isEqualTo(blockchain);
    }

    @Test
    void saveWalletAndTransactions() {
        var wallet = new Wallet();
        SnapshotCreator.loadPeer(me);
        SnapshotCreator.saveSerializedWallet(wallet);

        assertThat(walletFile.exists()).isTrue();

        final var walletOptional = DataLoader.readWallet();
        assertThat(walletOptional).isPresent();
        var loadedWallet = walletOptional.get();

        final var recovered = loadedWallet.getKeyPair();

        assertThat(loadedWallet).isEqualTo(wallet);
        assertThat(recovered.getPublic()).isEqualTo(wallet.getKeyPair().getPublic());
        assertThat(recovered.getPrivate()).isEqualTo(wallet.getKeyPair().getPrivate());

        final var pool = new TransactionPool();
        final var t1 = new Transaction(wallet, new PublicAddress(Keys.generateKeys().getPublic()), BigDecimal.valueOf(2L));
        final var t2 = new Transaction(wallet, new PublicAddress(Keys.generateKeys().getPublic()), BigDecimal.valueOf(3L));
        pool.addOrUpdate(t1);
        pool.addOrUpdate(t2);

        pool.snapshot();
        assertThat(DataLoader.readTransactionPool()).isPresent().get().isEqualTo(pool);
    }

    @Test
    void testSavePeers() {
        var fairy = Fairy.create();
        var localhost = "localhost";
        var peers = new Peers(Set.of(
                new Peer(fairy.person().getFirstName(), (int) (Math.random() * 10), localhost),
                new Peer(fairy.person().getFirstName(), (int) (Math.random() * 10), localhost),
                new Peer(fairy.person().getFirstName(), (int) (Math.random() * 10), localhost)));

        peers.snapshot();

        assertThat(DataLoader.readPeers()).isPresent().get().isEqualTo(peers);
    }

    @AfterEach
    void tearDown() {
        blockchainFile.delete();
        walletFile.delete();
        transactionPoolFile.delete();
        peersFile.delete();
        dirFile.delete();

        assertThat(blockchainFile).doesNotExist();
        assertThat(walletFile).doesNotExist();
        assertThat(transactionPoolFile).doesNotExist();
        assertThat(peersFile).doesNotExist();
        assertThat(dirFile).doesNotExist();
    }
}