package pl.edu.uj.gbartnicka.blockchainsimulator.hooks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.io.File;
import java.nio.file.Paths;

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

    @Mock
    private Peer me;

    @BeforeEach
    void setUp() {
        when(me.getHost()).thenReturn("localhost");
        when(me.getPort()).thenReturn(Integer.valueOf(port));
    }

    @Test
    void saveBlockchain() {
        var blockchain = new Blockchain();
        blockchain.setDifficulty(1);
        blockchain.addBlock(new Block(1, "asdf"));

        SnapshotCreator.loadPeer(me);
        SnapshotCreator.save(blockchain.toJson(), blockchain.filename());

        assertThat(blockchainFile.exists()).isTrue();

        DataLoader.loadPeer(me);
        final var readBlockchain = DataLoader.readBlockchain();

        assertThat(readBlockchain).isPresent().get().isEqualTo(blockchain);
    }

    @Test
    void saveWallet() {
        var wallet = new Wallet();
        SnapshotCreator.loadPeer(me);
        SnapshotCreator.saveSerializedWallet(wallet);

        assertThat(walletFile.exists()).isTrue();

        final var walletOptional = DataLoader.readWallet();
        assertThat(walletOptional).isPresent();
        var loadedWallet = walletOptional.get();

        final var recovered = loadedWallet.getKeyPair();

        assertThat(loadedWallet).isEqualTo(wallet);
        assertThat(recovered.getPublic()).isEqualTo(wallet.getPublicKey());
        assertThat(recovered.getPrivate()).isEqualTo(wallet.getKeyPair().getPrivate());
    }

    @AfterEach
    void tearDown() {
        blockchainFile.delete();
        walletFile.delete();
        dirFile.delete();

        assertThat(blockchainFile).doesNotExist();
        assertThat(walletFile).doesNotExist();
        assertThat(dirFile).doesNotExist();
    }
}