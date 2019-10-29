package com.example.product;

import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for {@link Product products}. */
public interface ProductRepository extends JpaRepository<Product, Long> {}
