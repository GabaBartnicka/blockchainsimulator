package pl.edu.uj.gbartnicka.blockchainsimulator.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.uj.gbartnicka.blockchainsimulator.utils.Jsonable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse implements Jsonable {
    private String status;

}
