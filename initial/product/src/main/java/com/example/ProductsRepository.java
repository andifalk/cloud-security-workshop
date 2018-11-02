package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Product products}.
 */
public interface ProductsRepository extends JpaRepository<Product,Long> {
}
