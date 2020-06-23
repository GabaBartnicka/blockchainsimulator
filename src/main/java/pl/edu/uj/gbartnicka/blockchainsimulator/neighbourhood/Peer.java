package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer implements Jsonable {
    private String name;
    private Integer port;
    private String host;

    public Peer(@NotNull String hostname, int port) {
        this.host = hostname;
        this.port = port;
        this.name = "[" + hostname + ":" + port + "]";
    }

    @Contract("_ -> new")
    public static @NotNull Peer resolve(@NotNull String property) {
        String[] split = property.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid peer property:" + property);
        }
        var host = split[0];
        var port = Integer.valueOf(split[1]);
        return new Peer("P" + port, port, host);
    }
}
