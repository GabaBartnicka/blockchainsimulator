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

import java.security.Security;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class BlockchainSimulatorApplication {
    @Value("${spring.rsocket.server.port}")
    int port;

    @Value("${app.hostname}")
    String hostname;


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
        return Try.of(() -> DataLoader.readBlockchain().orElseGet(Blockchain::new)).getOrElse(Blockchain::new);
    }

    @Bean
    Wallet wallet() {
        return Try.of(() -> DataLoader.readWallet().orElseGet(Wallet::new)).getOrElse(Wallet::new);
    }

    @Bean
    TransactionPool transactionPool() {
        return Try.of(() -> DataLoader.readTransactionPool().orElseGet(TransactionPool::new)).getOrElse(TransactionPool::new);
    }
}
