package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Pageable<T> {
    List<T> ranged(@NotNull Integer from, @NotNull Integer to);
}
