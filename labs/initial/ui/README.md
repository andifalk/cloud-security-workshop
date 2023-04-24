# Product Client Frontend UI

Now we will implement the corresponding client for the product server to show the product list in a web UI.

> __Tip__:  
> You may look into the [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security-oauth2-client)
> and the [Spring Security Reference Documentation](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#oauth2client) on how to implement a client.

To start with this tutorial part, navigate to the project `labs/initial/ui` in your IDE.

First just run this unfinished client. Please make sure that you also have started the product server from previous 
part.

Just run class `com.example.UiApplication`. Then navigate your web browser to http://localhost:9095/client.
You should see the following screen.

![Client main screen](images/client_main_screen.png)

Now try to click the link for _Products_. This should lead to the following whitelabel error screen:

![Client main screen](images/forbidden_error.png)

This is because our initial client still only sends a basic authentication header to authenticate the request for getting the
product list. But the product server now requires a JWT token instead. This is why we now get a 401 http status error (unauthorized).

So let's start with fixing this issue by implementing an OAuth2/OIDC client. 

## Step 1: Change Maven dependencies for the client

Add the following dependency to the existing maven _pom.xml_ file:

<u>_pom.xml_:</u>
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

This adds the spring boot starter dependency for building an OAuth2/OIDC client.
This includes all required classes to manage the authorization code flow of OAuth2 and 
handle all JWT token related tasks.

## Step 2: Add required properties for the client

The client requires several configuration parameters from the identity server to be used.
Thanks to the OpenID Connect discovery specification most identity servers publish all required 
parameters at a well known server endpoint `/.well-known/openid-configuration`.
In case of _Spring Authorization Server_ the url is http://localhost:9000/.well-known/openid-configuration.

This is why one parameter of spring (see below) is requiring the _issuer-uri_. This points to the
base url address of the identity server (i.e. without the _/.well-known/openid-configuration_ part).

Spring security provides predefined properties to configure the application as an OAuth2/OIDC client:

* The property ```spring.security.oauth2.client.provider.spring.issuer-uri``` specifies 
the URI for loading the required configuration to setup an OAuth2/OIDC client for the _Spring Authorization Server_ 
identity provider.
* The property ```spring.security.oauth2.client.provider.user-name-attribute``` specifies
  the attribute claim to use for mapping user data retrieved from user info endpoint in OAuth2/OIDC client for the _Auth0_
  identity provider.
* The property ```spring.security.oauth2.client.registration.spring.client-id``` specifies 
the _client id_ as it has been registered at the _Spring Authorization Server_ identity provider.
* The property ```spring.security.oauth2.client.registration.spring.clientAuthenticationMethod``` specifies 
the authentication method to use when calling the token endpoint at the _Spring Authorization Server_ identity provider. The value of _NONE_ specifies that no _client_secret_ is specified, instead the dynamic _Proof Key for Key Exchange (PKCE)_ is used instead. 
* The property ```spring.security.oauth2.client.registration.spring.authorizationGrantType``` specifies 
which OAuth2/OIDC grant flow should be used for the client.
* The property ```spring.security.oauth2.client.registration.spring.redirect-uri``` specifies 
the redirect URI to call our client application with the authorization code 
from the _Auth0_ identity provider. Spring also provides predefined placeholders for the base url and the registration id.
* The property ```spring.security.oauth2.client.registration.spring.scope``` specifies
  the scopes to be used for the OAuth2 login. The value of _openid_ enables the OpenID Connect mode and _profile_/_email_ specifies which attribute claims to include in the token.

After adding the required new properties the updated _application.yml_ should look like this:

<u>_application.yml_:</u>
```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          spring:
            issuer-uri: http://localhost:9000
            user-name-attribute: name
        registration:
          spring:
            client-id: 'demo-client-pkce'
            authorizationGrantType: authorization_code
            clientAuthenticationMethod: NONE
            redirect-uri: '{baseUrl}/login/oauth2/code/{registrationId}'
            scope:
              - openid
              - profile
              - email
```

> __Important:__    
> Please check that all indents are correct. Otherwise, you may get strange runtime errors when starting
> the application.
> The client secret is noted here just for the purpose of this tutorial. In your real productive applications 
> you should __NEVER__ publish sensitive data like this client secret or any other sensitive data!!

## Step 3: Add OAuth2/OIDC client security configuration 

To enable the client application to act as a OAuth2/OIDC client for _Auth0_ identity provider
it is required to add a new security configuration.

To achieve this, create a new class named _WebSecurityConfiguration_ in package _com.example_. 

<u>_com/example/WebSecurityConfiguration.java_:</u>

```java
package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                .oauth2Client().and()
                .oauth2Login(withDefaults()).logout().invalidateHttpSession(true);
        return http.build();
    }
}
```

## Step 4: Update the call to the resource server

We already extended the product server requiring a bearer token in the _Authorization_ header with each request.
To be able to call the server from the client we need to add the access token.

To achieve this we have to change the class _ProductService_ to add the required header with the token.

<u>_com/example/ProductService.java_:</u>

```java
package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private final String productUrl;
    private final RestTemplate template = new RestTemplate();

    public ProductService(@Value("${product.server.url}") String productUrl) {
        this.productUrl = productUrl;
    }

    public Collection<Product> getAllProducts(OAuth2AccessToken oAuth2AccessToken) {

        ResponseEntity<Product[]> response;

        LOG.info("Calling product server at [{}]", productUrl);

        try {
            response =
                    template.exchange(
                            productUrl + "/v1/products",
                            GET,
                            new HttpEntity<Product[]>(createAuthorizationHeader(oAuth2AccessToken)),
                            Product[].class);

            LOG.info("Successfully called product server products rest API");

            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException ex) {
            LOG.error("Error calling called product server products rest API", ex);
            if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401)) || ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(403))) {
                throw new AccessDeniedException("You are not authorized to call this");
            } else {
                throw ex;
            }
        }
    }

    public Collection<ProductUser> getAllProductUsers(OAuth2AccessToken oAuth2AccessToken) {

        ResponseEntity<ProductUser[]> response;

        LOG.info("Calling product server at [{}]", productUrl);

        try {
            response = template.exchange(
                    productUrl + "/v1/users",
                    GET,
                    new HttpEntity<ProductUser[]>(createAuthorizationHeader(oAuth2AccessToken)),
                    ProductUser[].class);

            LOG.info("Successfully called product server user rest API");

            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException ex) {
            LOG.error("Error calling called product server user rest API", ex);
            if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401)) || ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(403))) {
                throw new AccessDeniedException("You are not authorized to call this");
            } else {
                throw ex;
            }
        }
    }
    
    private HttpHeaders createAuthorizationHeader(OAuth2AccessToken oAuth2AccessToken) {
        return new HttpHeaders() {
            {
                String authHeader = "Bearer " + oAuth2AccessToken.getTokenValue();
                set("Authorization", authHeader);
            }
        };
    }
}
```

In the _ProductController_ class we need to add  a reference to an instance of class _OAuth2AuthorizedClientService_.
By using this instance we can retrieve the required access token.

In addition to this we also show the currently authenticated user by adding a new parameter of 
type _org.springframework.security.oauth2.core.oidc.user.OidcUser_ annotated by _@AuthenticationPrincipal_.

<u>_com/example/ProductController_:</u>

```java
package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UI controller for products frontend.
 */
@Controller
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    
    public ProductController(ProductService productService, OAuth2AuthorizedClientService authorizedClientService) {
        this.productService = productService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping(path = "/")
    public String index(@AuthenticationPrincipal OidcUser oidcUserInfo, Model model) {
        String fullName = oidcUserInfo.getUserInfo().getFullName();
        model.addAttribute("username", fullName);
        return "index";
    }

    @GetMapping(path = "/products")
    public String getAllProducts(Authentication authentication, Model model) {
        OAuth2AccessToken accessToken = resolveAccessToken(authentication);
        Iterable<Product> products = productService.getAllProducts(accessToken);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/users")
    public String getAllUsers(Authentication authentication, Model model) {
        OAuth2AccessToken accessToken = resolveAccessToken(authentication);
        Iterable<ProductUser> users = productService.getAllProductUsers(accessToken);
        model.addAttribute("users", users);
        return "users";
    }

    private OAuth2AccessToken resolveAccessToken(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            OAuth2AuthorizedClient authorizedClient =
                    this.authorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), authentication.getName());
            if (authorizedClient != null) {
                LOG.info("Client is authorized {}", authorizedClient.getPrincipalName());
                return authorizedClient.getAccessToken();
            } else {
                LOG.warn("Client is not authorized");
                throw new BadCredentialsException("Invalid Token");
            }
        } else {
            LOG.warn("Unexpected authentication type {}", authentication);
            throw new BadCredentialsException("Invalid Token");
        }
    }
}
```

Please note that _"auth0"_ refers to the corresponding id of the client configuration in _application.yml_.

## Step 5: Run the client application

Now we can run the finished client as well. Please make sure that you also have started the product server from previous part.

Just run class `com.example.UiApplication`. Then navigate your web browser to http://localhost:9095/client.

If you have successfully followed and completed all steps you should be redirected to the login dialog of the identity server of _Spring Authorization Server_.

![Auth0 Login](images/login.png)

To login please use the following user credentials:

* user: bwayne
* password: wayne

> __Important:__    
> The user credentials are noted here just for the purpose of this tutorial. In your real productive applications 
> you should __NEVER__ publish user credentials or any other sensitive data!!

After successful login you should again be redirected back to the client application, and you should see
the main screen.

![Client main screen](images/client_main_screen.png)

After clicking the _Products_ link you should see the list of products.

![Client products screen](images/client_products.png)

By clicking the _Users_ link you will get the list of registered users. You might get an access-denied error. Please try the another user with the `ADMIN` role.

![Client users screen](images/client_users.png)

This ends the whole tutorial.

## License

Apache 2.0 licensed
Copyright (c) by 2023 Andreas Falk

[1]:http://www.apache.org/licenses/LICENSE-2.0.txt
