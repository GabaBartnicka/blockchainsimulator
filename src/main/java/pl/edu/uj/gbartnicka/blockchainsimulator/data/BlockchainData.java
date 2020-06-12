package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.Data;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;

import java.util.List;

@Data
public class BlockchainData implements Jsonable {
    private List<Transaction> transactions;
    private String message;

    public BlockchainData(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BlockchainData(String message) {
        this.message = message;
    }
}
