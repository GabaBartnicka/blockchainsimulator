package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface Jsonable {
    default String toJson() {
        return new Gson().toJson(this);
    }

    default String toPrettyJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
