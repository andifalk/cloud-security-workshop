package com.example.productuser;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.FetchType.EAGER;

@Entity
public class ProductUserEntity extends AbstractPersistable<Long> {

    @NotEmpty
    @Size(min = 1, max = 100)
    @Column(unique = true)
    private String userId;

    @Size(min = 1, max = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    private String lastName;

    @Size(min = 3, max = 100)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @ElementCollection(fetch = EAGER)
    private List<String> roles = new ArrayList<>();

    public ProductUserEntity() {
        // For JPA
    }

    public ProductUserEntity(ProductUser productUser) {
        this.userId = productUser.getUserId();
        this.firstName = productUser.getFirstName();
        this.lastName = productUser.getLastName();
        this.password = productUser.getPassword();
        this.email = productUser.getEmail();
        this.roles = productUser.getRoles();
    }

    public ProductUserEntity(
            String userId,
            String firstName,
            String lastName,
            String password,
            String email,
            List<String> roles) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductUserEntity that = (ProductUserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(password, that.password) && Objects.equals(email, that.email) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, firstName, lastName, password, email, roles);
    }

    @Override
    public String toString() {
        return "ProductUserEntity{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    public ProductUser toProductUser() {
        return new ProductUser(getUserId(), getFirstName(), getLastName(), getPassword(), getEmail(), getRoles());
    }
}
