package com.vinstore.topup.repository;

import com.vinstore.topup.entity.TopupOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopupOrderRepository extends JpaRepository<TopupOrder, Long> {

    Optional<TopupOrder> findByOrderCode(String orderCode);
}
