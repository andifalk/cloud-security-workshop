package com.example.authorizationcode.client.web;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TokenResponse {

  private String access_token;
  private String id_token;
  private String refresh_token;
  private int expires_in;
  private String token_type;
  private String scope;

  public String getAccess_token() {
    return access_token;
  }

  public String getDecodedAccessToken() {
    if (getAccess_token() != null) {
      return new String(Base64.getDecoder().decode(getAccess_token()), UTF_8);
    } else {
      return "N/A";
    }
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public int getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
  }

  public String getToken_type() {
    return token_type;
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public void setId_token(String id_token) {
    this.id_token = id_token;
  }

  public String getId_token() {
    return id_token;
  }
}
