package pl.edu.uj.gbartnicka.blockchainsimulator.utils.hooks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveBlockchainToFileTest {

    @InjectMocks
    private SnapshotHandler saveBlockchainToFile;

    @Mock
    private Peer me;

    @Test
    void saveTest() {
        when(me.getHost()).thenReturn("localhost");
        when(me.getPort()).thenReturn(8080);

        saveBlockchainToFile.save();

        assertThat(Paths.get("src", "main", "resources", "db", "8080_localhost_db.txt").toFile().exists()).isTrue();
    }

    @AfterEach
    void tearDown() {
        Paths.get("src", "main", "resources", "db", "8080_localhost_db.txt").toFile().delete();
    }
}