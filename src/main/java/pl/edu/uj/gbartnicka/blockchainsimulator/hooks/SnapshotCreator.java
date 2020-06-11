package pl.edu.uj.gbartnicka.blockchainsimulator.hooks;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class SnapshotCreator {
    private static Peer PEER;

    public static void loadPeer(@NotNull Peer peer) {
        PEER = peer;
        Path filePathDirectory = Paths.get("src", "main", "resources", "db");
        if (!filePathDirectory.toFile().exists()) {
            final var mkdir = filePathDirectory.toFile().mkdir();
            log.info("Directory {} created={}", filePathDirectory.toString(), mkdir);
        }

        var peerBased = PEER.getHost() + "_" + PEER.getPort();

        final var peerBasedFile = Paths.get("src", "main", "resources", "db", peerBased);
        if (!peerBasedFile.toFile().exists()) {
            final var peerBasedMkdir = peerBasedFile.toFile().mkdir();
            log.info("Directory {} created={}", peerBasedFile.toString(), peerBasedMkdir);
        }
    }

    public static void save(@NotNull String json, @NotNull String filename) {
        log.info("Saving into the file: {}", filename);
        Path filePath = pathDirectoryPeerBased(filename);
        Try.of(() -> Files.writeString(filePath, json, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))
                .onFailure(e -> log.error("Cannot save into the file {} - {}", filePath, e.getMessage(), e))
                .onSuccess(b -> log.info("Saved into the file {}", filePath));
    }

    @NotNull
    public static Path pathDirectoryPeerBased(@NotNull String filename) {
        var peerBased = PEER.getHost() + "_" + PEER.getPort();
        Path filePathDirectory = Paths.get("src", "main", "resources", "db", peerBased);
        if (!filePathDirectory.toFile().exists()) {
            final var mkdir = filePathDirectory.toFile().mkdir();
            log.info("Directory {} created={}", filePathDirectory.toString(), mkdir);
        }
        return Paths.get("src", "main", "resources", "db", peerBased, filename);
    }

    public static void saveSerializedWallet(@NotNull Wallet wallet) {
        var fullPath = pathDirectoryPeerBased(wallet.filename());

        Try.run(() -> {
            var fileOutputStream = new FileOutputStream(fullPath.toFile());
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(wallet);
            objectOutputStream.flush();
            objectOutputStream.close();
        })
                .onFailure(e -> log.error("Cannot save into the file {} - {}", fullPath, e.getMessage(), e))
                .onSuccess(b -> log.info("Saved into the file {}", fullPath));
    }
}
