package com.vinstore.topup.service;

import com.vinstore.topup.dto.GameResponse;
import com.vinstore.topup.dto.ProductResponse;
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
public class CatalogService {

    private final GameRepository gameRepository;
    private final ProductRepository productRepository;

    public CatalogService(GameRepository gameRepository, ProductRepository productRepository) {
        this.gameRepository = gameRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<GameResponse> listGames() {
        return gameRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GameResponse getGame(String slug) {
        return toResponse(findActiveGame(slug));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listProducts(String slug) {
        Game game = findActiveGame(slug);
        return productRepository.findByGameIdAndActiveTrueOrderByPriceAsc(game.getId()).stream()
                .map(this::toProductResponse)
                .toList();
    }

    private Game findActiveGame(String slug) {
        return gameRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game tidak ditemukan"));
    }

    private GameResponse toResponse(Game g) {
        return new GameResponse(g.getId(), g.getName(), g.getSlug(), g.getImageUrl(), g.getUserIdLabel());
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
