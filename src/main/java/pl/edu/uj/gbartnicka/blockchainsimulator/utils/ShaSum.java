package pl.edu.uj.gbartnicka.blockchainsimulator.utils;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

@Slf4j
public class ShaSum {

    public static String sha256(@NotNull String string) {
        var messageDigest = Try.of(() -> MessageDigest.getInstance("SHA-256"))
                .onFailure(e -> log.error("Problem with messageDigest: {}", e.getMessage(), e))
                .get();

        byte[] hashed = messageDigest.digest(string.getBytes());
        return new String(Hex.encode(hashed));
    }
}
