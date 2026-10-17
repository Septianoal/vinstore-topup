package com.vinstore.topup.controller;

import com.vinstore.topup.dto.GameResponse;
import com.vinstore.topup.dto.ProductResponse;
import com.vinstore.topup.service.CatalogService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class CatalogApiController {

    private final CatalogService catalogService;

    public CatalogApiController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<GameResponse> listGames() {
        return catalogService.listGames();
    }

    @GetMapping("/{slug}/products")
    public List<ProductResponse> listProducts(@PathVariable String slug) {
        return catalogService.listProducts(slug);
    }
}