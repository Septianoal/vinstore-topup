package com.vinstore.topup.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderCodeGeneratorTest {

    @Test
    void kodeOrderMemilikiFormatYangBenar() {
        String code = OrderCodeGenerator.generate();

        assertThat(code).matches("VS-\\d{6}-[A-Z2-9]{6}");
    }

    @Test
    void kodeOrderTidakDuplikatPadaSeratusPercobaan() {
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            codes.add(OrderCodeGenerator.generate());
        }

        assertThat(codes).hasSize(100);
    }
}
