package com.vinstore.topup.sandbox;

import com.vinstore.topup.dto.PaymentView;
import com.vinstore.topup.service.PaymentService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@Profile("!prod")
@RequestMapping("/sandbox/pay")
public class SandboxPaymentController {

    private final PaymentService paymentService;
    private final SandboxGatewayClient gatewayClient;

    public SandboxPaymentController(PaymentService paymentService, SandboxGatewayClient gatewayClient) {
        this.paymentService = paymentService;
        this.gatewayClient = gatewayClient;
    }

    @GetMapping("/{gatewayRef}")
    public String page(@PathVariable String gatewayRef, Model model) {
        model.addAttribute("payment", paymentService.getPaymentView(gatewayRef));
        return "sandbox-pay";
    }

    @PostMapping("/{gatewayRef}/complete")
    public String complete(@PathVariable String gatewayRef, @RequestParam String result) {
        if (!result.equals("PAID") && !result.equals("FAILED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hasil tidak dikenal");
        }
        PaymentView payment = paymentService.getPaymentView(gatewayRef);
        if (payment.status().equals("PENDING")) {
            gatewayClient.sendWebhook(gatewayRef, result, payment.amount());
        }
        return "redirect:/orders/" + payment.orderCode();
    }
}
