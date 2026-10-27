package com.vinstore.topup.repository;

import com.vinstore.topup.entity.OrderStatus;
import com.vinstore.topup.entity.TopupOrder;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopupOrderRepository extends JpaRepository<TopupOrder, Long> {

    List<TopupOrder> findTop100ByOrderByCreatedAtDesc();

    List<TopupOrder> findTop100ByStatusOrderByCreatedAtDesc(OrderStatus status);
    
    Optional<TopupOrder> findByOrderCode(String orderCode);
}
