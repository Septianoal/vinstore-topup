package com.vinstore.topup.controller;

import com.vinstore.topup.dto.PaymentWebhookRequest;
import com.vinstore.topup.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class PaymentWebhookController {

    private final PaymentService paymentService;

    public PaymentWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public String receive(@Valid @RequestBody PaymentWebhookRequest request) {
        paymentService.handleWebhook(request);
        return "OK";
    }
}