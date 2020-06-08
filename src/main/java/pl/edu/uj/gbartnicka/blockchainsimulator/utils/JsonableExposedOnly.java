package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface JsonableExposedOnly extends Jsonable {
    @Override
    default String toPrettyJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    @Override
    default String toJson() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }


}
