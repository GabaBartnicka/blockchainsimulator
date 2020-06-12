package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.JsonableExposedOnly;

import java.io.Serializable;
import java.security.PublicKey;

import static pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys.decodePublicKey;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(exclude = "encoded")
public class PublicAddress implements JsonableExposedOnly, Serializable {

    private static final long serialVersionUID = 4138383763728995145L;

    @Expose
    @NotNull
    private final String name;

    @Expose
    @Nullable
    private String label;

    @Nullable
    private byte[] encoded;

    public PublicAddress(@NotNull PublicKey key) {
        this.name = Hex.toHexString(key.getEncoded());
        this.encoded = key.getEncoded();
    }

    public PublicAddress(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public PublicKey toPubKey() {
        return decodePublicKey(encoded);
    }

    public PublicAddress(@NotNull String name, @NotNull String label) {
        this.name = name;
        this.label = label;
    }

    public void attachLabel(@NotNull String label) {
        this.label = label;
    }
}
