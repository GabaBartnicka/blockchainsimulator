package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;

@Data
public class TransactionResponse {
    private Transaction transaction;
    private String errorMessage;

    public static @NotNull TransactionResponse of(Transaction transaction) {
        var response = new TransactionResponse();
        response.setTransaction(transaction);
        return response;
    }

    public static @NotNull TransactionResponse bad(String errorMessage) {
        var response = new TransactionResponse();
        response.setErrorMessage(errorMessage);
        return response;
    }
}
