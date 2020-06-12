package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Slf4j
public class BlockchainWallet extends Wallet {

    public BlockchainWallet() {
        super();
        publicAddress.attachLabel("blockchain-wallet");
    }

    @Override
    @NotNull
    public Transaction createTransaction(@NotNull PublicAddress recipient, @NotNull BigDecimal amount, @NotNull TransactionPool pool) {
        throw new UnsupportedOperationException("This operation is not supported for blockchain wallet");
    }
}
