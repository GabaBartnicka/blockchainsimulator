package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j
public class TransactionPool {
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

    public Optional<Transaction> findExistingForSenderAddress(@NotNull String publicAddress) {
        return transactions.stream().filter(t -> t.getInput().getSenderAddress().equals(publicAddress)).findAny();
    }
}
