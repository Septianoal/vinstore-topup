package com.vinstore.topup.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PaymentSignatureTest {

    private static final String SECRET = "rahasia-untuk-tes";
    private static final BigDecimal AMOUNT = new BigDecimal("10000");

    @Test
    void tandaTanganBenarDiterima() {
        String signature = PaymentSignature.sign(SECRET, "PAY-1", "PAID", AMOUNT);

        assertThat(PaymentSignature.matches(SECRET, "PAY-1", "PAID", AMOUNT, signature)).isTrue();
    }

    @Test
    void jumlahDenganDesimalBerbedaTetapSama() {
        String signature = PaymentSignature.sign(SECRET, "PAY-1", "PAID", AMOUNT);

        assertThat(PaymentSignature.matches(SECRET, "PAY-1", "PAID", new BigDecimal("10000.00"), signature)).isTrue();
    }

    @Test
    void jumlahYangDiubahDitolak() {
        String signature = PaymentSignature.sign(SECRET, "PAY-1", "PAID", AMOUNT);

        assertThat(PaymentSignature.matches(SECRET, "PAY-1", "PAID", new BigDecimal("1"), signature)).isFalse();
    }

    @Test
    void statusYangDiubahDitolak() {
        String signature = PaymentSignature.sign(SECRET, "PAY-1", "FAILED", AMOUNT);

        assertThat(PaymentSignature.matches(SECRET, "PAY-1", "PAID", AMOUNT, signature)).isFalse();
    }

    @Test
    void rahasiaBerbedaDitolak() {
        String signature = PaymentSignature.sign("rahasia-lain", "PAY-1", "PAID", AMOUNT);

        assertThat(PaymentSignature.matches(SECRET, "PAY-1", "PAID", AMOUNT, signature)).isFalse();
    }
}