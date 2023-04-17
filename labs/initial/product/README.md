# Product Server (Resource server)

__Tip__:  
You may look into the [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security-oauth2-server)
 and the [Spring Security Reference Documentation](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#oauth2resourceserver) on how to implement a resource server.

## Step 1: Change Maven dependencies for resource server

To start with this tutorial please navigate to the project __initial/product_ in your IDE.
This is the starting point for all following implementation steps.

The existing productEntity server is using the base spring security
lib to secure its endpoints using basic authentication and
form login.

To change these existing authentication mechanisms to JWT authentication as a resource server we need to adapt the 
spring security dependencies, i.e. use the corresponding one for building a secure OAuth2/OIDC resource server instead of simple
basic authentication.

To perform this required change replace the following dependency in the existing maven _pom.xml_ file:

<u>_pom.xml_:</u>
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

with this new dependency:

<u>_pom.xml_:</u>
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

## Step 2: Add required properties for resource server

The resource server requires the public key(s) to validate the signature of incoming JSON web tokens (JWT). This way nobody can
just issue their own JWT tokens or modify the issued token along the transmission path.
The public key(s) will be automatically grabbed from the JSON web key set provided by the
identity provider at https://access-me.eu.auth0.com/.well-known/jwks.json.

Spring security provides a predefined property ```spring.security.oauth2.resourceserver.jwt.jwt-set-uri``` 
to specify this.

After adding this new property the updated _application.yml_ should look like this:

<u>_application.yml_:</u>

```yaml
spring:
  jpa:
    open-in-view: false
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://access-me.eu.auth0.com/.well-known/jwks.json
```

__Important:__  
Please check that all indents are correct. Otherwise, you may get strange runtime errors when starting
the application.

## Step 3: Change security configuration for resource server

Please navigate to the class _com.example.security.WebSecurityConfiguration_ in your IDE
and change this with the following contents.

<u>_com.example.security.WebSecurityConfiguration.java_:</u>

```java
package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

  @Bean
  public SecurityFilterChain api(HttpSecurity http) throws Exception {
    http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
            .and()
            .httpBasic()
            .disable()
            .formLogin()
            .disable()
            .authorizeHttpRequests()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .oauth2ResourceServer()
            .jwt();
    return http.build();
  }
}
```

In this updated security configuration we

* disable web sessions as with token authentication each request must contain the token in the header and a session cookie is not required any more 
* disable [CSRF]() protection as we do not use session cookies any more and therefore are not vulnerable for CSRF attacks
* disable basic authentication and formular based login
* enable the application to act as an OAuth2/OIDC resource server requiring JWT tokens in the _authorization_ header

Please note that the bean definition for the ```PasswordEncoder``` has been removed as well as the password encoding
is not required any more.
This will cause compilation errors in ```ProductInitializer``` class. To solve these just remove all references 
to the encoder in that class.

<u>_com.example.ProductInitializer.java_:</u>

```java
package com.example;

import com.example.product.ProductEntityEntity;
import com.example.product.ProductEntityRepository;
import com.example.productuser.ProductUserEntity;
import com.example.productuser.ProductUserEntityRepository;
import com.example.productuser.ProductUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Stream;

/** Initializes some products in database. */
@Component
public class ProductInitializer implements CommandLineRunner {
 private static final Logger LOG = LoggerFactory.getLogger(ProductInitializer.class.getName());

 private final ProductEntityRepository productEntityRepository;
 private final ProductUserEntityRepository productUserEntityRepository;

 public ProductInitializer(
         ProductEntityRepository productEntityRepository, ProductUserEntityRepository productUserEntityRepository) {
  this.productEntityRepository = productEntityRepository;
  this.productUserEntityRepository = productUserEntityRepository;
 }

 @Override
 public void run(String... strings) {
  Stream.of(
                  new ProductEntity("Apple", "A green apple", 3.50),
                  new ProductEntity("Banana", "The perfect banana", 7.00),
                  new ProductEntity("Orange", "Lots of sweet oranges", 33.00),
                  new ProductEntity("Pineapple", "Exotic pineapple", 1.50),
                  new ProductEntity("Grapes", "Red wine grapes", 10.75))
          .forEach(productEntityRepository::save);

  LOG.info("Created " + productEntityRepository.count() + " products");

  Stream.of(
                  new ProductUserEntity(
                          "auth0|5bc44fceb144eb0173391741",
                          "Uwe",
                          "User",
                          "n/a",
                          "user@example.com",
                          Collections.singletonList("USER")),
                  new ProductUserEntity(
                          "auth0|5bc4b1553385d56f61f70e3b",
                          "Alex",
                          "Admin",
                          "n/a",
                          "admin@example.com",
                          Collections.singletonList("ADMIN")))
          .forEach(productUserEntityRepository::save);

  LOG.info("Created " + productUserEntityRepository.count() + " users");
 }
}
```

## Step 4: Convert the JWT into the ProductUser

With the changes of step 3 the base configuration for a resource server is set up.
But there is one issue with this change.
In class ```com.example.productEntity.ProductRestController``` we do not get _ProductUser_ as input for _@AuthenticationPrincipal_, 
instead by default the class _org.springframework.security.oauth2.jwt.Jwt_ will be provided as input. 

```  
@RestController
public class ProductRestController {
  ...
  @GetMapping(path = "/products")
  public List<Product> getAllProducts(@AuthenticationPrincipal(errorOnInvalidType = true) ProductUser productUserEntity) {
    ...
  }
}
``` 

To change this behavior we have to add our own converter from the JWT token to the _ProductUser_ class.
This is done in several steps.

First we need to define our own type for _AuthenticationToken_. This is the central point where Spring Security stores all
authentication details after authentication has been successfully performed.

<u>_com.example.security.ProductUserAuthenticationToken_:</u>

```java
package com.example.security;

import com.example.productuser.ProductUserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ProductUserAuthenticationToken extends AbstractAuthenticationToken {

    private final ProductUserEntity productUserEntity;

    public ProductUserAuthenticationToken(ProductUserEntity productUserEntity, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.productUserEntity = productUserEntity;
    }

    @Override
    public Object getCredentials() {
        return "n/a";
    }

    @Override
    public Object getPrincipal() {
        return this.productUserEntity;
    }
}
```

The previous class will now be used as part of the _ProductJwtAuthenticationConverter_.
This converts contents of the JWT token into attributes of our _ProductUser_.

<u>_com.example.security.ProductJwtAuthenticationConverter_:</u>

```java
package com.example.security;

import com.example.productuser.ProductUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ProductJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    @Autowired
    public ProductJwtAuthenticationConverter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
        if (userDetails instanceof ProductUserEntity) {
            return new ProductUserAuthenticationToken((ProductUserEntity) userDetails, userDetails.getAuthorities());
        } else {
            return null;
        }
    }
}
```

Also, the existing _ProductUserDetailsService_ class has to be changed because now we will use
the attribute _userid_ to identify the user in our database instead of _email_. The user id is given to us as _subject_ claim inside the JWT token.

<u>_com.example.security.ProductUserDetailsService_:</u>

```java
package com.example.security;

import com.example.productuser.ProductUserEntity;
import com.example.productuser.ProductUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ProductUserDetailsService implements UserDetailsService {

    private final ProductUserService productUserService;

    @Autowired
    public ProductUserDetailsService(ProductUserService productUserService) {
        this.productUserService = productUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProductUserEntity user = productUserService.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "No user could be found for user name '" + username + "'");
        }

        return user;
    }
}
```

Finally, we have to add this new _ProductJwtAuthenticationConverter_ to our security configuration.

<u>_com.example.security.WebSecurityConfiguration.java_:</u>

```java
package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final ProductJwtAuthenticationConverter productJwtAuthenticationConverter;
  
  public WebSecurityConfiguration(ProductJwtAuthenticationConverter productJwtAuthenticationConverter) {
    this.productJwtAuthenticationConverter = productJwtAuthenticationConverter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic()
        .disable()
        .formLogin()
        .disable()
        .authorizeRequests()
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .oauth2ResourceServer()
        .jwt().jwtAuthenticationConverter(productJwtAuthenticationConverter);
  }
}
```

## Step 5: Run the productEntity server application

Now we are ready to start the productEntity server.
Select the class _com.example.ProductApplication_ and run this (use the right mouse button in your IDE or the spring boot dashboard if applicable).

To test the REST Api (http://localhost:9090/server/products) of the running productEntity server we will use
Postman. You may also use command line tools like _curl_ or _httpie_ as well.

After starting Postman you can create a new collection by clicking the button _New Collection_ on the left.
Then you can add a new request by clicking the 3 dots next to the collection and select _Add Request_.

Just fill in the URL you see in the picture below.

![Postman request](images/postman_request.png)

If you now click send then you will get a 401 error because
the JWT token is missing to access this endpoint.

To get such a token navigate to the tab _Authorization_ on the request screen and click on the _Get New Access Token_ button.
Then you will see a dialog as shown in the picture below.

![Postman get new access token](images/postman_get_access_token.png)

Just fill in the required values from the table below and then click on _Request Token_:

| Input            | Value                                                            |
|------------------|------------------------------------------------------------------|
| Grant Type       | Password Credentials                                             |
| Access Token URL | https://access-me.eu.auth0.com/oauth/token                       |
| Username         | user@example.com                                                 |
| Password         | user_4demo!                                                      |
| Client ID        | v13BSQLEZnw4N96V36dDdsGRd022isKe                                 |
| Client Secret    | Rf9cHEZge0LLbMsAPIEDPhdVZ4OeDFU-DSxfcacUh2lvxuzYGmYLaH54ZX1-cmNL |

After you got a token you can close this dialog and try again to send the request.
This time it should work, and you should see a list of products as JSON response.

In the next step we will make accessing the backend service a bit more user-friendly by enabling a provided client frontend to retrieve products from the backend using OAuth2/OIDC access tokens. 
