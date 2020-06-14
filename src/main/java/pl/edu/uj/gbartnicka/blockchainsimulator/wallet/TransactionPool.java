package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.JsonableExposedOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Slf4j
public class TransactionPool implements DisposableBean, JsonableExposedOnly {
    @Expose
    private List<Transaction> transactions = new ArrayList<>();

    public void addOrUpdate(@NotNull Transaction transaction) {
        var any = transactions.stream().filter(t -> t.getId().equals(transaction.getId())).findAny();
        any.ifPresentOrElse(existingTransaction -> {
                    var replaced = Collections.replaceAll(transactions, existingTransaction, transaction);
                    log.info("Transaction found {} and replaced {}", existingTransaction.getId().toString(), replaced);
                },
                () -> {
                    log.info("Adding new transaction {}", transaction.getId().toString());
                    transactions.add(transaction);
                });
    }

    public Optional<Transaction> findExistingForSenderAddress(@NotNull PublicAddress publicAddress) {
        return transactions.stream().filter(t -> t.getInput().getSenderAddress().equals(publicAddress)).findAny();
    }

    @Override
    public void destroy() {
        snapshot();
    }

    @NotNull
    public List<Transaction> validTransactions() {
        log.info("Fetching valid transactions");
        return transactions.stream().filter(Transaction::isValid).collect(Collectors.toList());
    }

    public void clear() {
        transactions.clear();
    }
}
