package com.vinstore.topup.service;

import com.vinstore.topup.dto.OrderForm;
import com.vinstore.topup.dto.OrderResponse;
import com.vinstore.topup.entity.Game;
import com.vinstore.topup.entity.Product;
import com.vinstore.topup.entity.TopupOrder;
import com.vinstore.topup.repository.GameRepository;
import com.vinstore.topup.repository.ProductRepository;
import com.vinstore.topup.repository.TopupOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final GameRepository gameRepository;
    private final ProductRepository productRepository;
    private final TopupOrderRepository orderRepository;

    public OrderService(GameRepository gameRepository,
                        ProductRepository productRepository,
                        TopupOrderRepository orderRepository) {
        this.gameRepository = gameRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(String slug, OrderForm form) {
        if (slug == null || slug.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Slug game wajib diisi");
        }
        if (form == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data order wajib diisi");
        }
        if (form.getProductId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pilih nominal top up terlebih dahulu");
        }

        String gameUserId = form.getGameUserId();
        String contact = form.getContact();
        if (gameUserId == null || gameUserId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID game wajib diisi");
        }
        if (contact == null || contact.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email atau nomor WhatsApp wajib diisi");
        }

        Game game = gameRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game tidak ditemukan"));

        Product product = productRepository.findById(form.getProductId())
                .filter(p -> p.getGame().getId().equals(game.getId()) && p.isActive())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk tidak  ditemukan"));

        TopupOrder order = new TopupOrder(
                OrderCodeGenerator.generate(),
                product,
                gameUserId.trim(),
                contact.trim());
        orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderCode) {
        if (orderCode == null || orderCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kode order wajib diisi");
        }

        TopupOrder order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order tidak ditemukan"));
        return toResponse(order);
    }

    private OrderResponse toResponse(TopupOrder o) {
        if (o == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order tidak valid");
        }

        Product p = o.getProduct();
        if (p == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Produk order tidak valid");
        }

        Game productGame = p.getGame();
        if (productGame == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Game produk tidak valid");
        }

        return new OrderResponse(
                o.getOrderCode(),
                productGame.getName(),
                p.getName(),
                o.getGameUserId(),
                o.getContact(),
                o.getTotalPrice(),
                o.getStatus().name(),
                o.getCreatedAt());
    }
}