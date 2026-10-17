package com.vinstore.topup.repository;

import com.vinstore.topup.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByGameIdAndActiveTrueOrderByPriceAsc(Long gameId);
}