package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Exclude;
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
    @Exclude
    private byte[] encoded;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PublicAddress(@JsonProperty("name") String name,
                         @JsonProperty("label") String label,
                         @JsonProperty("encoded") byte[] encoded) {
        this.name = name;
        this.label = label;
        this.encoded = encoded;
    }

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
