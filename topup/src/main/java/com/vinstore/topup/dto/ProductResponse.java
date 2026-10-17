package com.vinstore.topup.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, BigDecimal price) {
}