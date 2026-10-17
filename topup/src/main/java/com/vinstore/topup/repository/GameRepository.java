package com.vinstore.topup.repository;

import com.vinstore.topup.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByActiveTrueOrderByNameAsc();

    Optional<Game> findBySlugAndActiveTrue(String slug);
}