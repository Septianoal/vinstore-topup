package com.vinstore.topup.service;

import com.vinstore.topup.dto.GameForm;
import com.vinstore.topup.dto.ProductForm;
import com.vinstore.topup.entity.Game;
import com.vinstore.topup.entity.Product;
import com.vinstore.topup.repository.GameRepository;
import com.vinstore.topup.repository.ProductRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminCatalogService {

    private final GameRepository gameRepository;
    private final ProductRepository productRepository;

    public AdminCatalogService(GameRepository gameRepository, ProductRepository productRepository) {
        this.gameRepository = gameRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Game> listGames() {
        return gameRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Game getGame(Long id) {
        return findGame(id);
    }

    @Transactional(readOnly = true)
    public GameForm getGameForm(Long id) {
        Game game = findGame(id);
        GameForm form = new GameForm();
        form.setName(game.getName());
        form.setSlug(game.getSlug());
        form.setImageUrl(game.getImageUrl());
        form.setUserIdLabel(game.getUserIdLabel());
        form.setActive(game.isActive());
        return form;
    }

    @Transactional(readOnly = true)
    public boolean slugTaken(String slug, Long ignoreId) {
        return ignoreId == null
                ? gameRepository.existsBySlug(slug)
                : gameRepository.existsBySlugAndIdNot(slug, ignoreId);
    }

    @Transactional
    public void createGame(GameForm form) {
        gameRepository.save(new Game(
                form.getName().trim(),
                form.getSlug().trim(),
                emptyToNull(form.getImageUrl()),
                form.getUserIdLabel().trim()));
    }

    @Transactional
    public void updateGame(Long id, GameForm form) {
        findGame(id).update(
                form.getName().trim(),
                form.getSlug().trim(),
                emptyToNull(form.getImageUrl()),
                form.getUserIdLabel().trim(),
                form.isActive());
    }

    @Transactional(readOnly = true)
    public List<Product> listProducts(Long gameId) {
        return productRepository.findByGameIdOrderByPriceAsc(gameId);
    }

    @Transactional
    public void createProduct(Long gameId, ProductForm form) {
        Game game = findGame(gameId);
        productRepository.save(new Product(game, form.getName().trim(), form.getPrice()));
    }

    @Transactional(readOnly = true)
    public Long gameIdOfProduct(Long productId) {
        return findProduct(productId).getGame().getId();
    }

    @Transactional
    public void updateProduct(Long productId, ProductForm form) {
        findProduct(productId).update(form.getName().trim(), form.getPrice(), form.isActive());
    }

    private Game findGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game tidak ditemukan"));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk tidak ditemukan"));
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
