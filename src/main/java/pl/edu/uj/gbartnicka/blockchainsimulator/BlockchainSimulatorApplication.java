package pl.edu.uj.gbartnicka.blockchainsimulator;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataLoader;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Wallet;

import java.security.Security;

@Slf4j
@SpringBootApplication
public class BlockchainSimulatorApplication {
    @Value("${spring.rsocket.server.port}")
    int port;

    @Bean
    Peer myself() {
        return new Peer(port);
    }

    @Bean
    Blockchain blockchain() {
        return DataLoader.readBlockchain(myself()).orElseGet(Blockchain::new);
    }

    @Bean
    Wallet wallet() {
        return DataLoader.readWallet(myself()).orElseGet(Wallet::new);
    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication.run(BlockchainSimulatorApplication.class, args);
    }

}
