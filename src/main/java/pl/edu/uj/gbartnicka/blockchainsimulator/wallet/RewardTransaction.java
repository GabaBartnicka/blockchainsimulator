package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.MINING_REWARD;

public class RewardTransaction extends Transaction {

    public RewardTransaction(@NotNull Wallet minerWallet, @NotNull Wallet blockchainWallet) {
        super(UUID.randomUUID());
        addOutputsAndSign(blockchainWallet, new Output(minerWallet.getPublicAddress(), MINING_REWARD));
    }

    @Override
    public boolean isValid() {
        return verify();
    }

    @Override
    public boolean isReward() {
        return true;
    }
}
