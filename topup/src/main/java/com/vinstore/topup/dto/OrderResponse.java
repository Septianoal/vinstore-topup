package com.vinstore.topup.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        String orderCode,
        String gameName,
        String productName,
        String gameUserId,
        String contact,
        BigDecimal totalPrice,
        String status,
        LocalDateTime createdAt) {
}