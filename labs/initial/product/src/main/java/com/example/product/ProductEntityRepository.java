package com.example.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link ProductEntity products}.
 */
public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findOneByName(String name);
}
