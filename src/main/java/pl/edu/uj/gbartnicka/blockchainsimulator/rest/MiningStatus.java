package pl.edu.uj.gbartnicka.blockchainsimulator.rest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiningStatus {
    private long timestamp;
    private Boolean busy;
}
