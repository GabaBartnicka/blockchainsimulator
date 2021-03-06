package pl.edu.uj.gbartnicka.blockchainsimulator.hooks;

import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peers;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static pl.edu.uj.gbartnicka.blockchainsimulator.hooks.SnapshotCreator.pathDirectoryPeerBased;

@Slf4j
public class DataLoader {

    public static Optional<Blockchain> readBlockchain() {
        final var filePath = pathDirectoryPeerBased(Blockchain.class.getSimpleName().toLowerCase() + "_db.txt");
        return readBlockchain(filePath);
    }

    public static Optional<Blockchain> readBlockchain(Path filePath) {
        return Try.of(() -> Files.readString(filePath))
                  .onSuccess(b -> log.info("Loaded blockchain from {}", filePath))
                  .onFailure(e -> log.error("Cannot read file {}", filePath))
                  .map(json -> Optional.of(new Gson().fromJson(json, Blockchain.class)))
                  .getOrElse(Optional::empty);
    }

    public static Optional<Wallet> readWallet() {
        final var filePath = pathDirectoryPeerBased(Wallet.class.getSimpleName().toLowerCase() + "_db.txt");
        return readWallet(filePath);
    }

    public static Optional<Wallet> readWallet(Path filePath) {
        return Try.of(() -> {
            var fileInputStream = new FileInputStream(filePath.toFile());
            var objectInputStream = new ObjectInputStream(fileInputStream);
            var wallet = (Wallet) objectInputStream.readObject();

            wallet.attachKeyPair(Keys.recover(wallet.getEncodedPriv(), wallet.getPublicAddress().toPubKey())
                                     .orElseThrow(() -> new IllegalArgumentException("Cannot recover wallet keys!")));
            objectInputStream.close();
            return Optional.of(wallet);
        })
                  .onSuccess(b -> log.info("Loaded wallet from {}", filePath))
                  .onFailure(e -> log.error("Cannot read file {}", filePath))
                  .getOrElse(Optional::empty);
    }


    public static Optional<TransactionPool> readTransactionPool() {
        final var filePath = pathDirectoryPeerBased(TransactionPool.class.getSimpleName().toLowerCase() + "_db.txt");
        return readTransactionPool(filePath);
    }

    public static Optional<TransactionPool> readTransactionPool(Path filePath) {
        return Try.of(() -> Files.readString(filePath))
                  .onSuccess(b -> log.info("Loaded transaction pool from {}", filePath))
                  .onFailure(e -> log.error("Cannot read file {}", filePath))
                  .map(json -> Optional.of(new Gson().fromJson(json, TransactionPool.class)))
                  .onFailure(e -> log.warn("Cannot un-json value: {}", e.getMessage()))
                  .getOrElse(Optional::empty);
    }

    public static Optional<Peers> readPeers() {
        final var filePath = pathDirectoryPeerBased(Peers.class.getSimpleName().toLowerCase() + "_db.txt");
        return readPeers(filePath);
    }

    public static Optional<Peers> readPeers(Path filePath) {
        return Try.of(() -> Files.readString(filePath))
                  .onSuccess(b -> log.info("Loaded peers from {}", filePath))
                  .onFailure(e -> log.error("Cannot read file {}", filePath))
                  .map(json -> Optional.of(new Gson().fromJson(json, Peers.class)))
                  .getOrElse(Optional::empty);
    }
}
