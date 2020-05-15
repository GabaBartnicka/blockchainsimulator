package pl.edu.uj.gbartnicka.blockchainsimulator.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage {
    private String content;
}
