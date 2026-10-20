package com.vinstore.topup.service;

import com.vinstore.topup.dto.PaymentView;
import com.vinstore.topup.dto.PaymentWebhookRequest;
import com.vinstore.topup.entity.OrderStatus;
import com.vinstore.topup.entity.Payment;
import com.vinstore.topup.entity.PaymentStatus;
import com.vinstore.topup.entity.TopupOrder;
import com.vinstore.topup.repository.PaymentRepository;
import com.vinstore.topup.repository.TopupOrderRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentService {

    private final TopupOrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final String webhookSecret;

    public PaymentService(TopupOrderRepository orderRepository,
                          PaymentRepository paymentRepository,
                          @Value("${app.payment.webhook-secret}") String webhookSecret) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.webhookSecret = webhookSecret;
    }

    @Transactional
    public String startPayment(String orderCode) {
        TopupOrder order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order tidak ditemukan"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order ini sudah tidak menunggu pembayaran");
        }
        return paymentRepository.findByOrderId(order.getId())
                .orElseGet(() -> paymentRepository.save(new Payment(order, newGatewayRef(), "SANDBOX")))
                .getGatewayRef();
    }

    @Transactional(readOnly = true)
    public PaymentView getPaymentView(String gatewayRef) {
        Payment payment = findByGatewayRef(gatewayRef);
        TopupOrder order = payment.getOrder();
        return new PaymentView(payment.getGatewayRef(), order.getOrderCode(), order.getTotalPrice(),
                payment.getStatus().name());
    }

    @Transactional
    public void handleWebhook(PaymentWebhookRequest request) {
        if (!PaymentSignature.matches(webhookSecret, request.gatewayRef(), request.status(),
                request.amount(), request.signature())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tanda tangan tidak valid");
        }

        Payment payment = findByGatewayRef(request.gatewayRef());
        TopupOrder order = payment.getOrder();

        if (order.getTotalPrice().compareTo(request.amount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jumlah pembayaran tidak sesuai");
        }
        if (payment.getStatus() != PaymentStatus.PENDING) {
            return;
        }

        switch (request.status()) {
            case "PAID" -> {
                payment.markPaid();
                order.markPaid();
            }
            case "FAILED" -> {
                payment.markFailed();
                order.markFailed();
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status tidak dikenal");
        }
    }

    private Payment findByGatewayRef(String gatewayRef) {
        return paymentRepository.findByGatewayRef(gatewayRef)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pembayaran tidak ditemukan"));
    }

    private String newGatewayRef() {
        return "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}