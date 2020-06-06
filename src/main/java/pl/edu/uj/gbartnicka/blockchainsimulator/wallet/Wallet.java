package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.google.gson.annotations.Expose;
import io.vavr.control.Try;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.JsonableExposedOnly;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.CannotSignDataException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Signature;

import static pl.edu.uj.gbartnicka.blockchainsimulator.configuration.DefaultValues.INITIAL_BALANCE;
import static pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys.generateKeys;

@Data
@Slf4j
public class Wallet implements JsonableExposedOnly, Serializable {
    private static final long serialVersionUID = 1436085228654294618L;

    private final transient KeyPair keyPair;
    private final PublicKey publicKey;
    private final byte[] encodedPriv;

    @Expose
    private final String publicAddress;
    @Expose
    private BigDecimal balance = INITIAL_BALANCE;

    public Wallet() {
        this.keyPair = generateKeys();
        this.publicKey = keyPair.getPublic();
        this.publicAddress = Hex.toHexString(keyPair.getPublic().getEncoded());
        this.encodedPriv = keyPair.getPrivate().getEncoded();
        log.info("New wallet created {}", publicAddress);
    }

    @NotNull
    public byte[] sign(@NotNull String hashData) {
        return Try.of(() -> {
            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
            ecdsaSign.initSign(keyPair.getPrivate());
            ecdsaSign.update(hashData.getBytes(StandardCharsets.UTF_8));
            return ecdsaSign.sign();
        })
                .onSuccess(b -> log.info("Data has signed"))
                .onFailure(e -> log.error("Cannot sign data! {}", e.getMessage()))
                .getOrElseThrow(e -> new CannotSignDataException(e.getMessage(), e));
    }

    @NotNull
    public Transaction createTransaction(@NotNull String recipient, @NotNull BigDecimal amount, @NotNull TransactionPool pool) {
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
}
