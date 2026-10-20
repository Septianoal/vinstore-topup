package com.vinstore.topup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentWebhookRequest(
        @NotBlank String gatewayRef,
        @NotBlank String status,
        @NotNull BigDecimal amount,
        @NotBlank String signature) {
}