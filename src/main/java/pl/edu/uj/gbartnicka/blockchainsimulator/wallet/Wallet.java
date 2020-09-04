package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import io.vavr.control.Try;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.springframework.beans.factory.DisposableBean;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.SnapshotCreator;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.JsonableExposedOnly;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.CannotSignDataException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Signature;
import java.util.List;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.INITIAL_BALANCE;
import static pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys.generateKeys;

@Data
@Slf4j
@JsonIgnoreProperties({"keyPair"})
public class Wallet implements JsonableExposedOnly, Serializable, DisposableBean {
    private static final long serialVersionUID = 1436085228654294618L;
    @Expose
    protected final PublicAddress publicAddress;
    private final byte[] encodedPriv;
    private transient KeyPair keyPair;
    @Expose
    private BigDecimal balance;

    @TestOnly
    public Wallet() {
        this(INITIAL_BALANCE);
    }

    public Wallet(@NotNull BigDecimal initialBalance) {
        this.keyPair = generateKeys();
        this.publicAddress = new PublicAddress(keyPair.getPublic());
        this.encodedPriv = keyPair.getPrivate().getEncoded();
        this.balance = initialBalance;
    }

    @NotNull
    public byte[] sign(@NotNull String hashData) {
        return Try.of(() -> {
            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
            ecdsaSign.initSign(keyPair.getPrivate());
            ecdsaSign.update(hashData.getBytes(StandardCharsets.UTF_8));
            return ecdsaSign.sign();
        })
                  .onSuccess(b -> log.debug("Data has signed"))
                  .onFailure(e -> log.error("Cannot sign data! {}", e.getMessage()))
                  .getOrElseThrow(e -> new CannotSignDataException(e.getMessage(), e));
    }

    @NotNull
    Transaction createTransaction(@NotNull PublicAddress recipient, @NotNull BigDecimal amount, @NotNull TransactionPool pool) {
        if (amount.compareTo(balance) > 0) {
            throw new BalanceExceededException(this, amount);
        }
        final var existingTransaction = pool.findExistingForSenderAddress(publicAddress);
        if (existingTransaction.isPresent()) {
            final var transaction = existingTransaction.get();
            transaction.update(this, recipient, amount);
            return transaction;
        } else {
            var transaction = new Transaction(this, recipient, amount);
            pool.addOrUpdate(transaction);
            return transaction;
        }
    }

    @Override
    public void destroy() {
        SnapshotCreator.saveSerializedWallet(this);
    }

    public void attachKeyPair(@NotNull KeyPair keyPair) {
        if (!keyPair.getPublic().equals(this.publicAddress.toPubKey())) {
            throw new IllegalArgumentException("Invalid key pair!");
        }
        this.keyPair = keyPair;
    }

    public void updateBalance(@NotNull List<Transaction> minedTransactions) {
        minedTransactions.stream()
                         .filter(t -> t.input.senderAddress.equals(publicAddress))
                         .max(Transaction::compareTo)
                         .ifPresentOrElse(t -> t.getOutputAmountForAddress(publicAddress)
                                                .ifPresentOrElse(amount -> this.balance = amount,
                                                        () -> log.warn("No balance to update")),
                                 () -> log.warn("No transaction found for current wallet"));

        minedTransactions.stream()
                         .filter(Transaction::isReward)
                         .findAny()
                         .ifPresentOrElse(rt-> rt.getOutputAmountForAddress(publicAddress)
                                                 .ifPresentOrElse(amount -> this.balance = this.balance.add(amount),
                                 () -> log.warn("No reward balance to update")),
                                 () -> log.warn("No reward transaction found for current wallet"));
    }
}
