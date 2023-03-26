package com.example.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product entity.
 */
@Entity
public class Product extends AbstractPersistable<Long> {

    private String name;
    private String description;
    private BigDecimal price;

    /**
     * For JPA.
     */
    public Product() {
    }

    /**
     * Constructor.
     *
     * @param name        product name
     * @param description product description
     * @param price       product price
     */
    public Product(String name, String description, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
