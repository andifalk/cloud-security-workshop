package com.example.productuser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {

    ProductUser findOneByUserId(String userId);

    ProductUser findOneByEmail(String email);
}
