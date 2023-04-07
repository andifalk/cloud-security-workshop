package com.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductEntityRepository productEntityRepository;

    @Autowired
    public ProductService(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @PreAuthorize("hasRole('USER')")
    public List<Product> findAll() {
        return productEntityRepository.findAll().stream().map(pe ->
                new Product(pe.getName(), pe.getDescription(), pe.getPrice())).toList();
    }


}
