package com.example.productuser;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Entity
public class ProductUser extends AbstractPersistable<Long> implements UserDetails {

  @NotBlank private String userId;

  @NotBlank private String firstName;

  @NotBlank private String lastName;

  @NotBlank private String password;

  @NotBlank private String email;

  @NotNull
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> roles = new ArrayList<>();

  public ProductUser() {
    // For JPA
  }

  public ProductUser(
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(
        getRoles().stream()
            .map(s -> "ROLE_" + s)
            .collect(Collectors.joining()));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
