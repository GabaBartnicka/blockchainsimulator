package pl.edu.uj.gbartnicka.blockchainsimulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;

@SpringBootApplication
public class BlockchainSimulatorApplication {
    @Value("${spring.rsocket.server.port}")
    int port;

    @Bean
    Peer myself() {
        return new Peer(port);
    }

    public static void main(String[] args) {
        SpringApplication.run(BlockchainSimulatorApplication.class, args);
    }
}
