package pl.edu.uj.gbartnicka.blockchainsimulator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.hooks.SnapshotHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockchainService {
    private final Blockchain blockchain;
    private final SnapshotHandler saveBlockchainToFile;

    @PreDestroy
    public void snapshot() {
        log.info("Destroying");
        saveBlockchainToFile.save();
    }

    @PostConstruct
    public void load() {
        saveBlockchainToFile.read().ifPresent(b -> blockchain.replaceChains(b.getChain()));
    }
}
