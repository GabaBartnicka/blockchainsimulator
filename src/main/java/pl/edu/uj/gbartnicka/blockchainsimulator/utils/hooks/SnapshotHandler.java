package pl.edu.uj.gbartnicka.blockchainsimulator.utils.hooks;

import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnapshotHandler {

    private final Peer me;
    private final Blockchain blockchain;

    public void save() {
        log.info("Saving");
        var filename = me.getPort() + "_" + me.getHost() + "_db.txt";
        Gson gson = new Gson();
        var json = gson.toJson(blockchain);

        Path filePathDirectory = Paths.get("src", "main", "resources", "db");
        if (!filePathDirectory.toFile().exists()) {
            filePathDirectory.toFile().mkdir();
        }

        Path filePath = Paths.get("src", "main", "resources", "db", filename);
        Try.of(() -> Files.writeString(filePath, json, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))
                .onFailure(e -> log.error("Cannot save into the file {} - {}", filePath, e.getMessage(), e))
                .onSuccess(b -> log.info("Saved into the file {}", filePath));
    }
}
