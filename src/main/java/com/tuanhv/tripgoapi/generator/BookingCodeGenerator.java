package com.tuanhv.tripgoapi.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Year;

@Component
public class BookingCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789".toCharArray();

    private static final int RANDOM_LENGTH = 8;

    public String generate() {
        StringBuilder suffix = new StringBuilder(RANDOM_LENGTH);

        for (int i = 0; i < RANDOM_LENGTH; i++) {
            suffix.append(ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
        }

        return "TG-%d-%s".formatted(
                Year.now().getValue(),
                suffix
        );
    }
}
