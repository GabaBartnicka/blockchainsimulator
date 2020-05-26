package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ShaSumTest {

    @Test
    @DisplayName("Sha-256 test")
    void sha256Test() {
        var plain = "Blockchain";
        var expected = "625da44e4eaf58d61cf048d168aa6f5e492dea166d8bb54ec06c30de07db57e1";

        var actual = ShaSum.sha256(plain);

        assertThat(actual).isEqualTo(expected);
    }
}