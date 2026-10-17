package com.vinstore.topup.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class OrderCodeGenerator {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyMMdd");

    private OrderCodeGenerator() {
    }

    public static String generate() {
        StringBuilder code = new StringBuilder("VS-")
                .append(LocalDate.now().format(DATE))
                .append('-');
        for (int i = 0; i < 6; i++) {
            code.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }
}