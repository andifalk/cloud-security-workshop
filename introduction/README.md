# Introduction into OAuth and OpenID Connect (OIDC)

This workshop introduces two of the most important federated identity methods and standards 
used in current software architectures:

* OAuth 2.0 with latest additions of OAuth 2.1 draft specifications
* OpenID Connect 1.0 (OIDC)

First, introduction tells more about digital identities and Identity and Access Management (IAM) systems in general.
Further sections will go deeper into details of OAuth 2.0/2.1, OpenID Connect 1.0 and authentication tokens.

## Digital Identities

The book [learning digital identities](https://www.oreilly.com/library/view/learning-digital-identity/9781098117689/) is correct when it states that _identity is bigger than you think_.
It is not just about a username and a password.
Identities are very old and well-known from the physical world like birth certificates or passports.

A digital identity contains data that uniquely describes a person or thing but also contains information
about the subject’s relationships to other entities.  
In the context of digital identities you have to deal with different areas like:

* Privacy
* Trust
* Authenticity 
* Confidentiality 
* Federation
* Identity architectures and ecosystems

Today, most companies buy their identity systems. Identity and access management (IAM) is now a very big industry.
The diagram below shows the configuration and operation phases of IAM and additionally the difference between the identity and access management parts.

![IAM phases](images/iam-phases.png)

(Source: Wikipedia)

Identity management (IdM) is the task of controlling information about users on computers. Access control is the enforcement of access rights defined as part of access authorization.

### Identity Architectures

The architecture of identity systems can be classified by its degree of autonomy for the participants.

__Administrative:__  
The vast majority of identity systems are implemented for the purpose of organizations and follow the administrative identity architecture type. The whole ruleset of operation, assigning identities, defining attributes, and sharing is determined by the organization.  

__User-Centered:__  
In the context of the internet users are required to have lots of different accounts. That is why user-centered identity systems have been established. By using protocols like OAuth 2.0 and OpenID Connect with social login (login with Facebook, Twitter, GitHub etc.) users are given a higher degree of autonomy. 

__Self-Sovereign:__  
Modern self-sovereign identity systems give full control to participants in terms of certifiable identifiers, choice on sharing attributes and relationships.

![Identity_System_Architectures](images/identity_system_architectures.png)

(Source: Learning Digital Identity)

## Federated Identities

User-centered identity architectures are using federated identity methods and standards to give the participants at least a limited ability to consent to share specific relationships and attributes.

<img src="images/federated_methods.png" width="500" height="800" alt="Federated identity methods and standard"/>

The most important federated methods and standards are described in the following sections with putting emphasis on OAuth and OpenID Connect.

### SAML 2.0

  [Security Assertion Markup Language (SAML)](https://wiki.oasis-open.org/security/FrontPage) is an XML-based markup language for security assertions (statements that service providers use to make access-control decisions). In March 2005, the current SAML 2.0 version was announced as an OASIS Standard.

### SCIM

The [System for Cross-domain Identity Management (SCIM)](https://www.simplecloud.info/) specification is designed to make managing user identities in cloud-based applications and services easier.
The current SCIM 2.0 version is built on a object model where a Resource is the common denominator and all SCIM objects are derived from it. It is defined by RFC 7643 and RCF 7644.

![SCIM model](images/scim_model.png)

### OAuth 2.0/2.1

The OAuth 2.0 authorization framework has been specified by the [Internet Engineering Task Force (IETF)](https://www.ietf.org/) as [RFC 6749](https://www.rfc-editor.org/rfc/rfc6749.html) in 2012.

Why has OAuth 2.0 has been developed at all, what has been the business case for this?

In the next picture you can see the problem we had before OAuth 2.0 was there.

![Reasons for OAuth](images/reasons_for_oauth.png)

For each end every service you want to use on the internet you had to create a separate user. In the end you had lots of users for all different services like Stackoverflow, some Photo service or any other such thing. This also led to another problem that people started to reuse the same (maybe insecure) credentials at different services (what is called credential stuffing). So if an attacker had stolen such credentials the attacker may have also success on other services.

With OAuth 2.0 you don't have to register a separate user anymore for each service. Instead, you can use one of the well-known OAuth 2.0 identity providers out there like Google, Facebook or GitHub. The advantage is that you only have one user for all these services. And because these identity providers require secure passwords and also support multi-factor authentication it is much more difficult to steal or brute force such credentials at these providers.

![Solution with OAuth](images/solution_with_oauth.png)

In 2020 the OAuth working group at the [IETF](https://datatracker.ietf.org/wg/oauth/about/) started to discuss new [general security best practices](https://www.ietf.org/archive/id/draft-ietf-oauth-security-topics-22.html) and specific recommendations for [browser based applications](https://www.ietf.org/archive/id/draft-ietf-oauth-browser-based-apps-13.html) (single page applications).  
This led to a new OAuth 2.1 version that currently is available as a [draft specification](https://www.ietf.org/archive/id/draft-ietf-oauth-v2-1-08.html) and may be finalized end of this year or next year (unfortunately the IETF does not work based on roadmaps).

> The OAuth 2.1 authorization framework enables an application to obtain limited access to a protected resource, 
> either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and an
> authorization service, or by allowing the application to obtain access on its own behalf.
> 
> (*OAuth 2.1 draft specification*)

#### Realworld Example

![OAuth2 Stackoverflow_Example](images/stackoverflow_login.png)

#### Roles

OAuth 2.1 defines four roles:

* __Resource Owner (The User)__:  
An entity capable of granting access to a protected resource. When the resource owner is a person, it is referred to as an end-user.

* __Resource Server (API)__:  
The server hosting the protected resources, capable of accepting and responding to protected resource requests using access tokens. The resource server is often accessible via an API (Rest, GraphQL etc.). Typical example here is backend service implemented in Java. 

* __User Agent (Device)__:  
The user agent is the device tha user interacts with to access the client application. This may be for example the web browser, a mobile phone or a tv device.

* __Client (Application)__:  
An application making protected resource requests on behalf of the resource owner and with its authorization. The term _client_ does not imply any particular implementation characteristics (e.g., whether the application executes on a server, a desktop, in a web browser, or other devices).

* __Authorization Server (OAuth Server)__:  
The server issuing access tokens to the client after successfully authenticating the resource owner and obtaining authorization.

![OAuth Roles](images/oauth_roles.png)

#### Access Tokens

Access tokens are credentials used to access protected resources.  An access token is a string representing an authorization issued to the client. The string is usually opaque to the client.  
Tokens  represent specific scopes and durations of access, granted by the resource owner, 
and enforced by the resource server and authorization server.

> OAuth 2.0/2.1 does __not__ define any token format (it may be a opaque token or a JWT).

Access tokens are transmitted to the resource server as bearer tokens via the _authorization_ http header to authenticate the client at the resource server as specified in [RFC 6750](https://www.rfc-editor.org/rfc/rfc6750.html). The name bearer token implies that every bearer (holder) of the token is authenticated to retrieve protected resources from the resource server. The resource server cannot distinguish between a valid or malicious client presenting the token.

![Opaque bearer token](images/oauth2_opaque_bearer_token.png)

```http request
GET /v1/customers HTTP/1.1
Authorization: Bearer RsT5OjbzRn430zqMLgV3Ia
Host: api.my-resource-server.com
```
Access tokens may be validated by the introspection endpoint of the authorization server.

#### Refresh Tokens

For security reasons the lifetime of access tokens should be kept short (typically between 5 and 30 minutes). To ensure usability, and keep the user logged in without having to visit the authorization server again, you may use refresh tokens. Usually a refresh token is returned together with the access token when scope _offline_access_ is given. The refresh token can be exchanged for a new access token, so every time just before the access token will expire the refresh token is ued to get a new access token. Usually this is not issued for Javascript applications due to security issues when storing refresh tokens in the browser storage. 

To request a new access token with a refresh token you have to issue a http post request like this one:

```http request
POST https://authorization-server.com/token
 grant_type=refresh_token&refresh_token=REFRESH_TOKEN&
 client_id=CLIENT_ID&client_secret=CLIENT_SECRET
```

Then the new access token is returned together with a new refresh token_ 

```json
{
"access_token": "RsT5OjbzRn430zqMLgV3Ia",
"expires_in": 3600,
"refresh_token": "64d049f8b21191e12522d5d96d5641af5e8"
}
```

#### Scopes

The authorization and token endpoints allow the client to specify the scope of the access request using the scope request parameter. In turn, the authorization server uses the scope response parameter to inform the client of the scope of the access token issued.

The value of the scope parameter is expressed as a list of space-delimited, case-sensitive strings. The strings are defined by the authorization server. If the value contains multiple space-delimited strings, their order does not matter, and each string adds an access range to the requested scope.

Typically, scopes authorize different access levels for APIs like for example the GitHub API or the Google Mail API.
A sample list of scopes looks like this `user:read notifications:read`. 

#### Protocol Flow

The following diagram shows the abstract OAuth 2.1 protocol flow.

![Abstract OAuth2 protocol flow](images/abstract_oauth2_flow.png)

Abstract is to be taken literally here as the description of the steps of the protocol flow is very difficult to understand: 

1. First the client requests authorization from the resource owner. 
2. Next the resource owner grants authorization to the requesting client. After successful authorization by the resource owner the client receives an authorization grant, which is a credential representing the resource owner's authorization. This is expressed by the concrete authorization grant type (protocol flow variant) described in the following sections.
3. The client requests an access token by authenticating with the authorization server and by presenting the authorization grant
4. The authorization server authenticates the client and validates the authorization grant, and if valid, issues an access token.
5. The client requests the protected resource from the resource server and authenticates by presenting the access token.
6. The resource server validates the access token, and if valid, serves the requested information.

This becomes clearer if we look at the concrete protocol flow variants, called the _authorization grants_.

#### Authorization Grants

The authorization grants (or protocol flow variants) reflect the different client types that are supported by OAuth 2.0/2.1.

__OAuth 2.0__ defines the following authorization grant types:

| Client Type                   | Confidentiality | Authorization Grant                 |
|-------------------------------|-----------------|-------------------------------------|
| Javascript clients (SPA)      | Public          | Implicit                            |
| Trusted clients (first party) | Confidential    | Resource Owner Password Credentials |
| Resource owner=Client         | Confidential    | Client Credentials                  |
| Server-side web clients       | Confidential    | Authorization Code                  |
| Mobile clients                | Public          | Authorization Code + PKCE           |


__OAuth 2.1__ only defines the following authorization grant types:

| Client Type                   | Confidentiality | Authorization Grant                 |
|-------------------------------|-----------------|-------------------------------------|
| Javascript clients (SPA)      | Public          | Authorization Code + PKCE           |
| Resource owner=Client         | Confidential    | Client Credentials                  |
| Server-side web clients       | Confidential    | Authorization Code                  |
| Mobile clients                | Public          | Authorization Code + PKCE           |

##### Resource Owner Password Grant (removed in OAuth 2.1)

The intention of this protocol variant was to make migration to OAuth 2.0 easier from other authentication mechanisms like basic authentication or form based authentication. Here still the client asks for user credentials and then sends these to the authorization server to get an access token.  
Originally was added to OAuth to enable migrating applications from HTTP Basic Auth or using a stored password to OAuth
This variant was removed from the OAuth 2.1 spec because it contradicts and all concepts of OAuth by insecurely exposing the credentials of the resource owner to the client.
Furthermore, adapting this grant to two-factor authentication (e.g. with WebAuthn) can be hard or impossible.

![OAuth2 ro_password credentials grant + PKCE](images/oauth2_ro_password_credentials_flow.png)

```http request
POST https://authz-server.com/token
 grant_type=password&username=USERNAME
 &password=PASSWORD&client_id=CLIENT_ID
```

##### Implicit Grant (removed in OAuth 2.1)  

The implicit grant historically has been used by single page applications running as javascript client in the web browser. As this grant type is causing the authorization server to issue access tokens in the authorization response it is vulnerable to access token leakage and access token replay. 

![OAuth2 implicit grant](images/oauth2_implicit_flow.png)

```http request
GET https://authz-server.com/authorize?
    response_type=token&client_id=abcd
    &redirect_uri=https://client.example.org/cb
    &state=af0ifjsldkj
```

```http request
HTTP/1.1 302 Found
Location: https://client.example.org/cb#access_token=SlAV32hkKG
&token_type=bearer&id_token=eyJ0...
&expires_in=3600&state=af0ifjsldkj
```


##### Client Credentials Grant

Client credentials are used as an authorization grant typically when the client is acting on its own behalf (the client is also the resource owner).
Typical clients are batch processing applications that run in a non-interactive mode without requiring a personal user account. This is comparable with authenticating using a technical user.

![OAuth2 client credentials grant](images/oauth2_client_credentials_flow.png)

```http request
POST /token HTTP/1.1
Host: authz-server.com
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
```

##### Authorization Code Grant

![OAuth2 authorization code grant](images/oauth2_authz_code_flow.png)

```http request
GET /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz
&redirect_uri=https://client.example.com/cb HTTP/1.1
Host: authz-server.com
```

```http request
HTTP/1.1 302 Found
Location: https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA
&state=xyz&iss=https://authz-server.com/
```

```http request
POST /token HTTP/1.1
Host: authz-server.com
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
&redirect_uri=https://client.example.com/cb
```

##### Authorization Code Grant + PKCE

![OAuth2 authorization code grant + PKCE](images/oauth2_authz_code_pkce_flow.png)

```http request
GET /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz
&redirect_uri=https://client.example.com/cb 
&code_challenge=6fdkQaPm51l13DSukcAH3Mdx7_ntecHYd1vi3n0hMZY
&code_challenge_method=S256 HTTP/1.1
Host: authz-server.com
```

```http request
HTTP/1.1 302 Found
Location: https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA
&state=xyz&iss=https://authz-server.com/
```

```http request
POST /token HTTP/1.1
Host: authz-server.com
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
&redirect_uri=https://client.example.com/cb
&code_verifier=3641a2d12d66101249cdf7a79c000c1f8c05d2aafcf14bf146497bed
```

### OpenID Connect 1.0

[OpenID Connect 1.0 (OIDC)](https://openid.net/specs/openid-connect-core-1_0.html) is a simple identity layer on top of the OAuth 2.0 protocol. It allows Clients to verify the identity of the End-User based on the authentication performed by an Authorization Server, as well as to obtain basic profile information about the End-User in an interoperable and REST-like manner.

![OAuth2 OpenID_Connect](images/oauth_openid_connect.png)

#### Roles

OIDC defines the same roles as in OAuth 2.0 but with different namings.
And it adds another token for identification: The ID token.

![OpenID_Connect_Roles](images/openid_connect_roles.png)


#### Hybrid Flow Grant

OIDC also adds another grant flow. This is basically a combination from the _implicit grant_ and the _authorization code grant_. The intention was that the client gets the ID token a bit faster to improve the duration of initial loading of the client.  

As today the typical SPA clients use server-side rendering techniques the performance of the initial loading is not a problem anymore. Therefore, the risk of the _implicit grant flow_ part again in the _hybrid flow_ is not needed anymore. So in OIDC the _authorization code grant_ with _PKCE_ is also recommended.

```http request
GET /authorize?response_type=code%20id_token&client_id=s6BhdRkqt3
&redirect_uri=https://client.example.org/cb
&scope=openid%20profile%20email
&nonce=n-0S6_WzA2Mj
&state=af0ifjsldkj HTTP/1.1
Host: authz-server.com
```

```http request
HTTP/1.1 302 Found
Location: https://client.example.org/cb#code=SplxlOBeZQQYbYS6WxSbIA
&id_token=eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkaz...&state=af0ifjsldkj
```

```http request
POST /token HTTP/1.1
  Host: authz-server.com
  Content-Type: application/x-www-form-urlencoded
  Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW

  grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
    &redirect_uri=https://client.example.org/cb
```

```json
{
"access_token": "SlAV32hkKG",
"token_type": "Bearer",
"refresh_token": "8xLOxBtZp8",
"expires_in": 3600,
"id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkaz..."
}
```

#### ID and Access Tokens

As you can see here, OIDC can issue 3 types of tokens:

* The ID token for user identification. This token must only be used on the client side
* The access token. This token must only be used on the server side (the resource server aka the backend API).
* The refresh token to get a new access token

![OpenID_Connect tokens](images/openid_connect_tokens.png)

With OIDC it is the first time the format of a token is really specified. It is mandatory for the ID token to be defined as a JSON Web Token (JWT).
A JWT consists of 3 parts:

1. The header telling that it is a JWT and specifying the algorithm for the token signature
2. The payload of the token, this is the most important part for clients as this contains all user information as attributes (the _claims_)
3. The signature of the token. The signatures ensure that the token can only be created by a valid issuer and cannot be changed without noticing it

All tokens are transmitted as bearer tokens using the `Authorization` header with the `Bearer' prefix. So every participant who presents a valid token as the _bearer_ is authenticated.

![JWT bearer token](images/oauth2_jwt_bearer_token.png)

```http request
GET /v1/customers HTTP/1.1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkaz...
Host: api.my-resource-server.com
```

The other type of token transmission is the _proof of possession token_. Here, the token is bound to a specific client using cryptographic concepts like for example mutual tls. If another client then presents this kind of token and cannot proof the binding then the token will be denied.

#### User Info Endpoint

OIDC also offers a standard user info endpoint. With this you have the choice to put all information into the ID token or just put some information in the token and ask the user info endpoint for the other user information.  

This way you can avoid overloading your tokens with too much information and mabye running into problems when the maximum of http header size is reached. And you can avoid privacy issues when transmitting tokens to other parties, that may not be trustworthy.

```http request
GET /userinfo HTTP/1.1
Host: server.example.com
Authorization: Bearer SlAV32hkKG
```

```http request
HTTP/1.1 200 OK
Content-Type: application/json

{
    "sub": "248289761001",
    "name": "Jane Doe",
    "given_name": "Jane",
    "family_name": "Doe",
    "preferred_username": "j.doe",
    "email": "janedoe@example.com",
    "picture": "http://example.com/janedoe/me.jpg"
}
```

#### Discovery

At last OIDC also offers a convenient endpoint with all required information a client or server needs to configure the interaction with the authorization server.

This discovery endpoint can be found on the `/.well-known/openid-configuration` endpoint on compliant authorization servers. Please check the [setup](../setup/README.md) section on the specific endpoints for different providers.

As an example Spring Security uses this endpoint to configure most part of the OAuth client automatically. The drawback of this is, that the authorization server must be online and reachable by the OAuth client at startup to load the configuration.