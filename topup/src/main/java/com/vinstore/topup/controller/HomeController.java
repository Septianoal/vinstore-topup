package com.vinstore.topup.controller;

import com.vinstore.topup.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private final CatalogService catalogService;

    public HomeController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("games", catalogService.listGames());
        return "index";
    }

    @GetMapping("/games/{slug}")
    public String gameDetail(@PathVariable String slug, Model model) {
        model.addAttribute("game", catalogService.getGame(slug));
        model.addAttribute("products", catalogService.listProducts(slug));
        return "game";
    }
}