package pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer {
    private String name;
    private Integer port;
}
