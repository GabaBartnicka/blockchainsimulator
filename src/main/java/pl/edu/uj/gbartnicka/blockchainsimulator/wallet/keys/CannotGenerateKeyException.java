package pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys;

public class CannotGenerateKeyException extends IllegalArgumentException {
    public CannotGenerateKeyException(String s, Throwable cause) {
        super(s, cause);
    }
}
