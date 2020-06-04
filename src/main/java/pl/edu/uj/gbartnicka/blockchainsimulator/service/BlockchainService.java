package pl.edu.uj.gbartnicka.blockchainsimulator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;

@Component
@RequiredArgsConstructor
public class BlockchainService {
    private final Blockchain blockchain;

}
