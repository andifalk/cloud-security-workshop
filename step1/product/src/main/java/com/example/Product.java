package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

/**
 * Product entity.
 */
@Entity
public class Product extends AbstractPersistable<Long> {

    private String name;
    private String description;
    private double price;

    /**
     * For JPA.
     */
    public Product() {
    }

    /**
     * Constructor.
     *
     * @param name product name
     * @param description product description
     * @param price product price
     */
    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @JsonIgnore
    @Override
    public Long getId() {
        return super.getId();
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return super.isNew();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

}
