package com.example.productuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductUserService {

    private final ProductUserRepository productUserRepository;

    @Autowired
    public ProductUserService(ProductUserRepository productUserRepository) {
        this.productUserRepository = productUserRepository;
    }

    @Transactional
    public ProductUser save(ProductUser productUser) {
        return productUserRepository.save(productUser);
    }

    public List<ProductUser> findAll() {
        return productUserRepository.findAll();
    }

    public ProductUser findByUserId(String userId) {
        return productUserRepository.findOneByUserId(userId);
    }

    public ProductUser findByEmail(String email) {
        return productUserRepository.findOneByEmail(email);
    }
}
