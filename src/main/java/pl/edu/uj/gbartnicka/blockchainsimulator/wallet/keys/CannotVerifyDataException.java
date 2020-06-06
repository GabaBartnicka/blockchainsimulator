package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

public class CannotVerifyDataException extends IllegalArgumentException {
    public CannotVerifyDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
