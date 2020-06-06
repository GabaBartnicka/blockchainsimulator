package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface JsonableExposedOnly {
    default String toPrettyJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }
}
