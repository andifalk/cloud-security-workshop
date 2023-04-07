package com.example.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product entity.
 */
@Entity
public class ProductEntity extends AbstractPersistable<Long> {

    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    private BigDecimal price;

    public ProductEntity() {
        // For JPA
    }

    public ProductEntity(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
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

    public Product toProduct() {
        return new Product(getName(), getDescription(), getPrice());
    }
}
