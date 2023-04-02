package com.example.productuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductUserEntityRepository extends JpaRepository<ProductUserEntity, Long> {

    Optional<ProductUserEntity> findOneByUserId(String userId);

    Optional<ProductUserEntity> findOneByEmail(String email);
}
