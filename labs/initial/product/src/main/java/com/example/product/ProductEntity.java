package com.example.product;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product entity.
 */
@Entity
public class ProductEntity extends AbstractPersistable<Long> {

    private String name;
    private String description;
    private BigDecimal price;

    public ProductEntity() {
        // For JPA
    }

    public ProductEntity(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
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
        ProductEntity productEntity = (ProductEntity) o;
        return Objects.equals(name, productEntity.name) && Objects.equals(description, productEntity.description) && Objects.equals(price, productEntity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, price);
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
