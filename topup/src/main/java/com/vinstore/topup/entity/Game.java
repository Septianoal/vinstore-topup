package com.vinstore.topup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "user_id_label", nullable = false, length = 50)
    private String userIdLabel;

    @Column(nullable = false)
    private boolean active;

    protected Game() {
    }

    public Game(String name, String slug, String imageUrl, String userIdLabel) {
        this.name = name;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.userIdLabel = userIdLabel;
        this.active = true;
    }

    public void update(String name, String slug, String imageUrl, String userIdLabel, boolean active) {
        this.name = name;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.userIdLabel = userIdLabel;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getImageUrl() { return imageUrl; }
    public String getUserIdLabel() { return userIdLabel; }
    public boolean isActive() { return active; }
}