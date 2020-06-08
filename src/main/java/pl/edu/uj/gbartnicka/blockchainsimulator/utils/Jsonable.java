package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.SnapshotCreator;

public interface Jsonable {
    default String toJson() {
        return new Gson().toJson(this);
    }

    default String toPrettyJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    default void snapshot() {
        SnapshotCreator.save(toJson(), filename());
    }

    @NotNull
    default String filename() {
        return this.getClass().getSimpleName().toLowerCase() + "_db.txt";
    }
}
