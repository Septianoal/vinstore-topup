package com.vinstore.topup.repository;

import com.vinstore.topup.entity.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByActiveTrueOrderByNameAsc();
    
    List<Game> findAllByOrderByNameAsc();

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Optional<Game> findBySlugAndActiveTrue(String slug);
}