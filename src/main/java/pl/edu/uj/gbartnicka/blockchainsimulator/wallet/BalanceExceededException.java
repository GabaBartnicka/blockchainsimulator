package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class BalanceExceededException extends IllegalArgumentException {
    public BalanceExceededException(@NotNull Wallet senderWallet, @NotNull BigDecimal amount) {
        super("Amount " + amount.toPlainString() + " exceeds balance which is " + senderWallet.getBalance().toPlainString());
    }
}
