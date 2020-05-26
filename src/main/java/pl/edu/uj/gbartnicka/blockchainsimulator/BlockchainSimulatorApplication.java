package pl.edu.uj.gbartnicka.blockchainsimulator;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

import java.security.Security;

@Slf4j
@SpringBootApplication
public class BlockchainSimulatorApplication {
    @Value("${spring.rsocket.server.port}")
    int port;

    public static void main(String[] args) {
        SpringApplication.run(BlockchainSimulatorApplication.class, args);
    }

    @Bean
    Peer myself() {
        return new Peer(port);
    }

    @Bean
    Blockchain blockchain() {
        return new Blockchain();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void postStartup() {
        log.info("Adding BouncyCastleProvider");
        Security.addProvider(new BouncyCastleProvider());
    }
}
