package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import com.google.gson.Gson;

public interface Jsonable {
    default String toJson() {
        return new Gson().toJson(this);
    }
}
