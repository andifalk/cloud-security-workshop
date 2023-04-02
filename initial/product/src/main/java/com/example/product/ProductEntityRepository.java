package com.example.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link ProductEntity products}.
 */
public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {
}
