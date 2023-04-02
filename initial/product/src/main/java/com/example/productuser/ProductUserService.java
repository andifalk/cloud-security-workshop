package com.example.productuser;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ProductUserEntity save(ProductUserEntity productUserEntity) {
        return productUserEntityRepository.save(productUserEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductUser> findAll() {
        return productUserEntityRepository.findAll().stream().map(pue ->
                new ProductUser(pue.getUserId(), pue.getFirstName(), pue.getLastName(),
                        pue.getPassword(), pue.getEmail(), pue.getRoles())).toList();
    }

    public Optional<ProductUser> findByUserId(String userId) {
        return productUserEntityRepository.findOneByUserId(userId).map(pue ->
                new ProductUser(pue.getUserId(), pue.getFirstName(), pue.getLastName(),
                        pue.getPassword(), pue.getEmail(), pue.getRoles()));
    }

    public Optional<ProductUser> findByEmail(String email) {
        return productUserEntityRepository.findOneByEmail(email).map(pue ->
                new ProductUser(pue.getUserId(), pue.getFirstName(), pue.getLastName(),
                        pue.getPassword(), pue.getEmail(), pue.getRoles()));
    }
}
