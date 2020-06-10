package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.JsonableExposedOnly;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.edu.uj.gbartnicka.blockchainsimulator.utils.ShaSum.sha256;

@Slf4j
@Data
public class Transaction implements JsonableExposedOnly {
    @Expose
    private final UUID id;
    @Expose
    private Input input;
    @Expose
    private List<Output> outputs = new ArrayList<>();

    public Transaction(@NotNull Wallet senderWallet, @NotNull String recipientAddress, @NotNull BigDecimal amount) {
        id = UUID.randomUUID();
        log.info("Created transaction with id: {}", id.toString());
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            log.error("Cannot create transaction");
            throw new BalanceExceededException(senderWallet, amount);
        }

        final var postBalance = senderWallet.getBalance().subtract(amount);
        final var senderInfo = new Output(senderWallet.getPublicAddress(), postBalance);
        final var recipientInfo = new Output(recipientAddress, postBalance);
        addOutput(senderInfo);
        addOutput(recipientInfo);
        attachSignature(senderWallet);
    }

    private void attachSignature(@NotNull Wallet senderWallet) {
        this.input = new Input(DateTime.now().getMillis(),
                senderWallet.getBalance(), senderWallet.getKeyPair().getPublic().getEncoded(), senderWallet.getPublicAddress(),
                senderWallet.sign(hashData()));
    }

    public boolean verify() {
        return Keys.verify(input.signature, hashData(), input.senderPublicKey);
    }

    public void addOutput(@NotNull Output output) {
        outputs.add(output);
    }

    public String hashData() {
        return sha256(new Gson().toJson(outputs));
    }

    public void update(@NotNull Wallet senderWallet, @NotNull String recipientAddress, @NotNull BigDecimal amount) {
        var senderAddress = senderWallet.getPublicAddress();
        final var recipientOpt = findSenderRelatedOutput(senderAddress);
        log.info("Updating transaction {} for sender {}", getId().toString(), senderWallet.getPublicAddress());
        if (recipientOpt.isEmpty()) {
            log.warn("Cannot update transaction for sender {}", senderWallet.getPublicAddress());
            return;
        }
        var senderOutput = recipientOpt.get();
        var oldAmount = senderOutput.getDeltaAmount();

        if (amount.compareTo(oldAmount) > 0) {
            log.error("Amount {} exceed balance which is {}", amount.toPlainString(), oldAmount.toPlainString());
            throw new BalanceExceededException(senderWallet, amount);
        }

        senderOutput.deltaAmount = senderOutput.deltaAmount.subtract(amount);
        addOutput(new Output(recipientAddress, amount));
        attachSignature(senderWallet);
    }

    @NotNull
    private Optional<Output> findSenderRelatedOutput(@NotNull String senderAddress) {
        return outputs.stream().filter(o -> o.getAddress().equals(senderAddress)).findAny();
    }

    @Data
    @AllArgsConstructor
    static class Input {
        @Expose
        final long timestamp;
        @Expose
        final BigDecimal amount;
        final byte[] senderPublicKey;
        @Expose
        final String senderAddress;
        final byte[] signature;
    }

    @Data
    @AllArgsConstructor
    static class Output {
        @Expose
        final String address;
        @Expose
        BigDecimal deltaAmount;
    }
}
