package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.PublicAddress;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private PublicAddress publicAddress;
    private BigDecimal amount;
}
