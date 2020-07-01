package pl.edu.uj.gbartnicka.blockchainsimulator;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataLoader;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.SnapshotCreator;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.math.BigDecimal;
import java.security.Security;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class BlockchainSimulatorApplication {
    @Value("${spring.rsocket.server.port}")
    int port;

    @Value("${app.hostname}")
    String hostname;

    @Value("${blockchain.initialDifficulty}")
    Integer initialDifficulty;

    @Value("${blockchain.mineRate}")
    Long mineRate;

    @Value("${blockchain.initialBalance}")
    BigDecimal initialBalance;

    public static void main(String[] args) {
        log.info("Params: {}", Stream.of(args).reduce((s, s2) -> s + ", " + s2).orElse("n/a"));
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication.run(BlockchainSimulatorApplication.class, args);
    }

    @Bean
    Peer myself() {
        final var peer = new Peer(hostname, port);
        log.info("You are {}", peer);
        SnapshotCreator.loadPeer(peer);
        return peer;
    }

    @Bean
    Blockchain blockchain() {
        final Supplier<Blockchain> orElseSuppler = () -> new Blockchain(initialDifficulty, mineRate);
        return Try.of(() -> DataLoader.readBlockchain().orElseGet(orElseSuppler)).getOrElse(orElseSuppler);
    }

    @Bean
    Wallet wallet() {
        final Supplier<Wallet> orElseSuppler = () -> new Wallet(initialBalance);
        return Try.of(() -> DataLoader.readWallet().orElseGet(orElseSuppler)).getOrElse(orElseSuppler);
    }

    @Bean
    TransactionPool transactionPool() {
        return Try.of(() -> DataLoader.readTransactionPool().orElseGet(TransactionPool::new))
                  .getOrElse(TransactionPool::new);
    }
}
