package com.vinstore.topup.controller;

import com.vinstore.topup.dto.OrderForm;
import com.vinstore.topup.dto.OrderResponse;
import com.vinstore.topup.service.CatalogService;
import com.vinstore.topup.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CatalogService catalogService;

    public OrderController(OrderService orderService, CatalogService catalogService) {
        this.orderService = orderService;
        this.catalogService = catalogService;
    }

    @PostMapping("/games/{slug}/order")
    public String createOrder(@PathVariable String slug,
                              @Valid @ModelAttribute("orderForm") OrderForm form,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("game", catalogService.getGame(slug));
            model.addAttribute("products", catalogService.listProducts(slug));
            return "game";
        }
        OrderResponse order = orderService.createOrder(slug, form);
        return "redirect:/orders/" + order.orderCode();
    }

    @GetMapping("/orders/{orderCode}")
    public String orderDetail(@PathVariable String orderCode, Model model) {
        model.addAttribute("order", orderService.getOrder(orderCode));
        return "order";
    }
}