package com.vinstore.topup.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TopupOrderTest {

    private TopupOrder newOrder() {
        return new TopupOrder("VS-261018-ABC234", new Product(), "12345", "a@contoh.com");
    }

    @Test
    void orderBaruBerstatusPending() {
        assertThat(newOrder().getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void orderPendingBisaMenjadiPaid() {
        TopupOrder order = newOrder();

        order.markPaid();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void orderYangSudahPaidTidakBisaDigagalkan() {
        TopupOrder order = newOrder();
        order.markPaid();

        assertThatThrownBy(order::markFailed).isInstanceOf(IllegalStateException.class);
    }
}
