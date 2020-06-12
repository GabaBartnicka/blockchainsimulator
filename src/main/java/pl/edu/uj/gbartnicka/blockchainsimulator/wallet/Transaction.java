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
import java.util.*;

import static pl.edu.uj.gbartnicka.blockchainsimulator.utils.ShaSum.sha256;

@Slf4j
@Data
public class Transaction implements JsonableExposedOnly {
    @Expose
    private final UUID id;
    @Expose
    protected Input input;
    @Expose
    protected List<Output> outputs = new ArrayList<>();

    protected Transaction(@NotNull UUID id) {
        this.id = id;
    }

    public Transaction(@NotNull Wallet senderWallet, @NotNull PublicAddress recipientAddress, @NotNull BigDecimal amount) {
        id = UUID.randomUUID();
        log.info("Created transaction with id: {}", id.toString());
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            log.error("Cannot create transaction");
            throw new BalanceExceededException(senderWallet, amount);
        }

        final var postBalance = senderWallet.getBalance().subtract(amount);
        final var senderInfo = new Output(senderWallet.getPublicAddress(), postBalance);
        final var recipientInfo = new Output(recipientAddress, amount);
        addOutputsAndSign(senderWallet, senderInfo, recipientInfo);
    }

    protected void addOutputsAndSign(@NotNull Wallet senderWallet, @NotNull Output... outputs) {
        this.outputs.addAll(Arrays.asList(outputs));
        attachSignature(senderWallet);
    }

    protected void attachSignature(@NotNull Wallet senderWallet) {
        this.input = new Input(DateTime.now().getMillis(),
                senderWallet.getBalance(), senderWallet.getPublicAddress(),
                senderWallet.sign(hashData()));
    }

    public boolean verify() {
        return Keys.verify(input.signature, hashData(), input.senderAddress.getEncoded());
    }

    public void addOutput(@NotNull Output output) {
        outputs.add(output);
    }

    public String hashData() {
        return sha256(new Gson().toJson(outputs));
    }

    public void update(@NotNull Wallet senderWallet, @NotNull PublicAddress recipientAddress, @NotNull BigDecimal amount) {
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

    public boolean isValid() {
        var outputSum = outputs.stream().map(Output::getDeltaAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!Objects.equals(outputSum, input.getAmount())) {
            log.warn("Invalid transaction from {}. Amounts malformed, i={}, o={}",
                    input.getSenderAddress(), input.getAmount(), outputSum);
            return false;
        }

        return verify();
    }

    @NotNull
    private Optional<Output> findSenderRelatedOutput(@NotNull PublicAddress senderAddress) {
        return outputs.stream().filter(o -> o.getAddress().equals(senderAddress)).findAny();
    }

    @Data
    @AllArgsConstructor
    static class Input {
        @Expose
        final long timestamp;
        @Expose
        final BigDecimal amount;
        @Expose
        final PublicAddress senderAddress;
        final byte[] signature;
    }

    @Data
    @AllArgsConstructor
    static class Output {
        @Expose
        final PublicAddress address;
        @Expose
        BigDecimal deltaAmount;
    }
}
