package com.vinstore.topup.dto;

import java.math.BigDecimal;

public record PaymentView(String gatewayRef, String orderCode, BigDecimal amount, String status) {
}