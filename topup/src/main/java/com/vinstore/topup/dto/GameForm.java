package com.vinstore.topup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GameForm {

    @NotBlank(message = "Nama game wajib diisi")
    @Size(max = 100, message = "Nama maksimal 100 karakter")
    private String name;

    @NotBlank(message = "Slug wajib diisi")
    @Size(max = 100, message = "Slug maksimal 100 karakter")
    @Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*", message = "Slug hanya huruf kecil, angka, dan tanda hubung")
    private String slug;

    @Size(max = 255, message = "Alamat gambar maksimal 255 karakter")
    private String imageUrl;

    @NotBlank(message = "Label input wajib diisi")
    @Size(max = 50, message = "Label maksimal 50 karakter")
    private String userIdLabel;

    private boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getUserIdLabel() { return userIdLabel; }
    public void setUserIdLabel(String userIdLabel) { this.userIdLabel = userIdLabel; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}