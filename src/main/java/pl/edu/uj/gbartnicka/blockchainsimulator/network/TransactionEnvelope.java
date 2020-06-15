package pl.edu.uj.gbartnicka.blockchainsimulator.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Envelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.Transaction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEnvelope implements Envelope {
    private Peer sender;
    private Transaction transaction;
}
