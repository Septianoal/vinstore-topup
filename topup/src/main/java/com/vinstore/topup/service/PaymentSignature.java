package com.vinstore.topup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class PaymentSignature {

    private static final String ALGORITHM = "HmacSHA256";

    private PaymentSignature() {
    }

    public static String sign(String secret, String gatewayRef, String status, BigDecimal amount) {
        String message = gatewayRef + "|" + status + "|" + amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM));
            return HexFormat.of().formatHex(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Gagal membuat tanda tangan", e);
        }
    }

    public static boolean matches(String secret, String gatewayRef, String status, BigDecimal amount, String signature) {
        String expected = sign(secret, gatewayRef, status, amount);
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8));
    }
}