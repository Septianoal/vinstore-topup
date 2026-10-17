package com.vinstore.topup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderForm {

    @NotNull(message = "Pilih nominal top up terlebih dahulu")
    private Long productId;

    @NotBlank(message = "User ID game wajib diisi")
    @Size(max = 100, message = "User ID maksimal 100 karakter")
    private String gameUserId;

    @NotBlank(message = "Email atau nomor WhatsApp wajib diisi")
    @Size(max = 100, message = "Maksimal 100 karakter")
    private String contact;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getGameUserId() { return gameUserId; }
    public void setGameUserId(String gameUserId) { this.gameUserId = gameUserId; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}