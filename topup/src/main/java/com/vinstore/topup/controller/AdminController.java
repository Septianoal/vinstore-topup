package com.vinstore.topup.controller;

import com.vinstore.topup.dto.GameForm;
import com.vinstore.topup.dto.ProductForm;
import com.vinstore.topup.entity.OrderStatus;
import com.vinstore.topup.service.AdminCatalogService;
import com.vinstore.topup.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminCatalogService catalogService;
    private final OrderService orderService;

    public AdminController(AdminCatalogService catalogService, OrderService orderService) {
        this.catalogService = catalogService;
        this.orderService = orderService;
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/games")
    public String games(Model model) {
        model.addAttribute("games", catalogService.listGames());
        return "admin/games";
    }

    @GetMapping("/games/new")
    public String newGame(Model model) {
        showGameForm(model, "Tambah game", "/admin/games", false);
        model.addAttribute("gameForm", new GameForm());
        return "admin/game-form";
    }

    @PostMapping("/games")
    public String createGame(@Valid @ModelAttribute("gameForm") GameForm form,
                             BindingResult result,
                             Model model) {
        checkSlug(form, null, result);
        if (result.hasErrors()) {
            showGameForm(model, "Tambah game", "/admin/games", false);
            return "admin/game-form";
        }
        catalogService.createGame(form);
        return "redirect:/admin/games";
    }

    @GetMapping("/games/{id}/edit")
    public String editGame(@PathVariable Long id, Model model) {
        showGameForm(model, "Ubah game", "/admin/games/" + id, true);
        model.addAttribute("gameForm", catalogService.getGameForm(id));
        return "admin/game-form";
    }

    @PostMapping("/games/{id}")
    public String updateGame(@PathVariable Long id,
                             @Valid @ModelAttribute("gameForm") GameForm form,
                             BindingResult result,
                             Model model) {
        checkSlug(form, id, result);
        if (result.hasErrors()) {
            showGameForm(model, "Ubah game", "/admin/games/" + id, true);
            return "admin/game-form";
        }
        catalogService.updateGame(id, form);
        return "redirect:/admin/games";
    }

    @GetMapping("/games/{id}/products")
    public String products(@PathVariable Long id, Model model) {
        showProducts(model, id);
        model.addAttribute("productForm", new ProductForm());
        return "admin/products";
    }

    @PostMapping("/games/{id}/products")
    public String createProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("productForm") ProductForm form,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            showProducts(model, id);
            return "admin/products";
        }
        catalogService.createProduct(id, form);
        return "redirect:/admin/games/" + id + "/products";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("productForm") ProductForm form,
                                BindingResult result,
                                RedirectAttributes redirect) {
        Long gameId = catalogService.gameIdOfProduct(id);
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Produk tidak disimpan: nama wajib diisi dan harga minimal Rp 1.000");
        } else {
            catalogService.updateProduct(id, form);
        }
        return "redirect:/admin/games/" + gameId + "/products";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false) String status, Model model) {
        OrderStatus filter = parseStatus(status);
        model.addAttribute("orders", orderService.listOrders(filter));
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("selected", filter == null ? "" : filter.name());
        return "admin/orders";
    }

    private void showGameForm(Model model, String title, String action, boolean editing) {
        model.addAttribute("formTitle", title);
        model.addAttribute("formAction", action);
        model.addAttribute("editing", editing);
    }

    private void showProducts(Model model, Long gameId) {
        model.addAttribute("game", catalogService.getGame(gameId));
        model.addAttribute("products", catalogService.listProducts(gameId));
    }

    private void checkSlug(GameForm form, Long ignoreId, BindingResult result) {
        if (form.getSlug() != null && !result.hasFieldErrors("slug")
                && catalogService.slugTaken(form.getSlug().trim(), ignoreId)) {
            result.rejectValue("slug", "duplicate", "Slug sudah dipakai game lain");
        }
    }

    private OrderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}