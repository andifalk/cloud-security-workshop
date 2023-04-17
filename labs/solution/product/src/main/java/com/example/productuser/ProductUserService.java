package com.example.productuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ProductUserService {

    private final ProductUserEntityRepository productUserEntityRepository;

    @Autowired
    public ProductUserService(ProductUserEntityRepository productUserEntityRepository) {
        this.productUserEntityRepository = productUserEntityRepository;
    }

    @Transactional
    public ProductUser save(ProductUser productUser) {
        return productUserEntityRepository.save(
                new ProductUserEntity(productUser)).toProductUser();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductUser> findAll() {
        return productUserEntityRepository.findAll().stream().map(ProductUserEntity::toProductUser).toList();
    }

    public Optional<ProductUser> findByUserId(String userId) {
        return productUserEntityRepository.findOneByUserId(userId).map(ProductUserEntity::toProductUser);
    }

    public Optional<ProductUser> findByEmail(String email) {
        return productUserEntityRepository.findOneByEmail(email).map(ProductUserEntity::toProductUser);
    }

    public Optional<ProductUser> findByProductUser(ProductUser productUser) {
        Example<ProductUserEntity> productUserEntityExample = Example.of(new ProductUserEntity(productUser));
        return productUserEntityRepository
                .findOne(productUserEntityExample)
                .map(ProductUserEntity::toProductUser);
    }
}
