package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.security.KeyPair;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.INITIAL_BALANCE;
import static pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.KeysGenerator.generateKeys;

@Data
@Slf4j
public class Wallet {
    private final KeyPair keyPair;
    private final String publicAddress;
    private BigDecimal balance = INITIAL_BALANCE;

    public Wallet() {
        this.keyPair = generateKeys();
        this.publicAddress = Hex.toHexString(keyPair.getPublic().getEncoded());
        log.info("New wallet created {}", publicAddress);
    }
}
