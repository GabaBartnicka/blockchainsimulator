package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

public class CannotSignDataException extends IllegalArgumentException {
    public CannotSignDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
