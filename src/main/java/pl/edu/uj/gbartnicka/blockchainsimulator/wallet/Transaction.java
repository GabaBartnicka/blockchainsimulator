package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Exclude;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.JsonableExposedOnly;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys;

import java.math.BigDecimal;
import java.util.*;

import static pl.edu.uj.gbartnicka.blockchainsimulator.utils.ShaSum.sha256;

@Slf4j
@Data
public class Transaction implements JsonableExposedOnly, Comparable<Transaction> {
    @Expose
    private final String id;
    @Expose
    protected Input input;
    @Expose
    protected List<Output> outputs = new ArrayList<>();
    @Expose
    protected Boolean valid;

    protected Transaction(@NotNull UUID id) {
        this.id = id.toString();
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Transaction(@JsonProperty("id") String id,
                       @JsonProperty("input") Input input,
                       @JsonProperty("outputs") List<Output> outputs,
                       @JsonProperty("valid") Boolean valid) {
        log.info("id: {}", id);
        this.id = id;
//        this.id = UUID.fromString(id);
//        this.id= UUID.randomUUID();
        this.input = input;
        this.outputs = outputs;
        this.valid = valid;
    }

    public Transaction(@NotNull Wallet senderWallet, @NotNull PublicAddress recipientAddress, @NotNull BigDecimal amount) {
        id = UUID.randomUUID().toString();
        log.debug("Created transaction with id: {}", id);
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            log.error("Cannot create transaction");
            throw new BalanceExceededException(senderWallet, amount);
        }

        final var postBalance = senderWallet.getBalance().subtract(amount);
        final var senderInfo = new Output(senderWallet.getPublicAddress(), postBalance);
        final var recipientInfo = new Output(recipientAddress, amount);
        addOutputsAndSign(senderWallet, senderInfo, recipientInfo);
        this.valid = isValid();
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
        log.debug("Updating transaction {} for sender {}", getId(), senderWallet.getPublicAddress());
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
        this.valid = isValid();
    }

    public boolean isValid() {
        var outputSum = allOutputsAmount();
        if (outputSum.compareTo(input.getAmount()) != 0) {
            log.warn("Invalid transaction from {}. Amounts malformed, i={}, o={}",
                    input.getSenderAddress(), input.getAmount(), outputSum);
            return false;
        }

        return verify();
    }

    @NotNull
    public BigDecimal allOutputsAmount() {
        return outputs.stream().map(Output::getDeltaAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @NotNull
    private Optional<Output> findSenderRelatedOutput(@NotNull PublicAddress senderAddress) {
        return outputs.stream().filter(o -> o.getAddress().equals(senderAddress)).findAny();
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        return (int) (timestamp() - o.timestamp());
    }

    public long timestamp() {
        return input.timestamp;
    }

    @Data
    static class Input {
        @Expose
        final long timestamp;
        @Expose
        final BigDecimal amount;
        @Expose
        final PublicAddress senderAddress;

        @Exclude
        final byte[] signature;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Input(@JsonProperty("timestamp") long timestamp,
                     @JsonProperty("amount") BigDecimal amount,
                     @JsonProperty("senderAddress") PublicAddress senderAddress,
                     @JsonProperty("signature") byte[] signature) {
            this.timestamp = timestamp;
            this.amount = amount;
            this.senderAddress = senderAddress;
            this.signature = signature;
        }
    }

    @Data
    static class Output {
        @Expose
        final PublicAddress address;
        @Expose
        BigDecimal deltaAmount;

        public Output(@JsonProperty("address") PublicAddress address,
                      @JsonProperty("deltaAmount") BigDecimal deltaAmount) {
            this.address = address;
            this.deltaAmount = deltaAmount;
        }
    }

    public Optional<BigDecimal> getOutputAmountForAddress(@NotNull PublicAddress publicAddress) {
        return outputs.stream().filter(o->o.getAddress().equals(publicAddress)).map(Output::getDeltaAmount).findAny();
    }

    public boolean isReward() {
        return false;
    }
}
