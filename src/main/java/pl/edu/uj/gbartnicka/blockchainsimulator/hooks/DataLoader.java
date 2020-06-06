package pl.edu.uj.gbartnicka.blockchainsimulator.hooks;

import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class DataLoader {

    public static Optional<Blockchain> readBlockchain(@NotNull Peer me) {
        var filename = me.getPort() + "_" + me.getHost() + "_db.txt";
        Path filePath = Paths.get("src", "main", "resources", "db", filename);

        return Try.of(() -> Files.readString(filePath))
                .onSuccess(b -> log.info("Loaded blockchain from {}", filePath))
                .onFailure(e -> log.error("Cannot read file {}", filePath))
                .map(json -> Optional.of(new Gson().fromJson(json, Blockchain.class)))
                .getOrElse(Optional.empty());
    }

    public static Optional<Wallet> readWallet(@NotNull Peer me) {
        var filename = me.getPort() + "_" + me.getHost() + "_wallet.txt";
        Path filePath = Paths.get("src", "main", "resources", "db", filename);

        return Try.of(() -> Files.readString(filePath))
                .onSuccess(b -> log.info("Loaded blockchain from {}", filePath))
                .onFailure(e -> log.error("Cannot read file {}", filePath))
                .map(json -> Optional.of(new Gson().fromJson(json, Wallet.class)))
                .getOrElse(Optional.empty());
    }
}
