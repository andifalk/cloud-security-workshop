# Reference Solution of Sample UI Applications for the Workshop

You can find the reference solution for the UI application of the workshop here.

To start the reference solution you have to provide the target identity provider as _spring profile_.
The following profiles are supported:

* __auth0__: Use Auth0 as identity provider
* __keycloak__: Use locally installed Keycloak as identity provider
* __spring__: Use locally installed custom Spring Authorization Server

## Maven

Start using `./mvnw spring-boot:run -Dspring.profiles.active=spring`

## IntelliJ

Specify the corresponding profile in the spring boot starter configuration (if the ultimate edition of IntelliJ is used). If you use the community edition specify the profile using an environment variable like `-Dspring.profiles.active=spring`.
