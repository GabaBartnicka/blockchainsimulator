package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.BlockchainWallet;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.MINE_RATE;

@RequiredArgsConstructor
@Data
public class BlockchainWithoutChain {

    private final int difficulty;
    private final long mineRate = MINE_RATE;
    private final int chains;
    private final BlockchainWallet wallet;

    @NotNull
    public static BlockchainWithoutChain fromBlockchain(@NotNull Blockchain blockchain) {
        return new BlockchainWithoutChain(blockchain.getDifficulty(), blockchain.getSize(), blockchain.getWallet());
    }
}
