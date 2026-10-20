package com.vinstore.topup.sandbox;

import com.vinstore.topup.dto.PaymentWebhookRequest;
import com.vinstore.topup.service.PaymentSignature;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Profile("!prod")
public class SandboxGatewayClient {

    private final RestClient restClient;
    private final String secret;

    public SandboxGatewayClient(@Value("${app.base-url}") String baseUrl,
                                @Value("${app.payment.webhook-secret}") String secret) {
        this.restClient = RestClient.create(baseUrl);
        this.secret = secret;
    }

    public void sendWebhook(String gatewayRef, String status, BigDecimal amount) {
        String signature = PaymentSignature.sign(secret, gatewayRef, status, amount);
        PaymentWebhookRequest body = new PaymentWebhookRequest(gatewayRef, status, amount, signature);
        restClient.post()
                .uri("/api/webhooks/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}