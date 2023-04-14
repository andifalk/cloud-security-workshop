# Introduction into OAuth 2.0/2.1 (OAuth2) and OpenID Connect (OIDC)

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
The diagram below shows the configuration and operation phases of IAM and additionally the difference between the 
identity and access management parts.

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

![IAM phases](images/identity_system_architectures.png)

(Source: Learning Digital Identity)

## Federated Identities Methods and Standards

User-centered identity architectures are using federated identity methods and standards to give the participants at least a limited ability to consent to share specific relationships and attributes.

Those methods and standards are:

* __Security Assertion Markup Language (SAML)__:
  [SAML](https://wiki.oasis-open.org/security/FrontPage) is an XML-based markup language for security assertions (statements that service providers use to make access-control decisions). In March 2005, the current SAML 2.0 version was announced as an OASIS Standard.
* __System for Cross-domain Identity Management (SCIM)__:
  The [System for Cross-domain Identity Management (SCIM)](https://www.simplecloud.info/) specification is designed to make managing user identities in cloud-based applications and services easier.
  The current SCIM 2.0 version is built on a object model where a Resource is the common denominator and all SCIM objects are derived from it. It is defined by RFC 7643 and RCF 7644.
* __OAuth 2.0__:
  [OAuth 2.0](https://www.rfc-editor.org/rfc/rfc6749.html) is the industry-standard protocol for authorization. OAuth 2.0 focuses on client developer simplicity while providing specific authorization flows for web applications, desktop applications, mobile phones, and other devices.
* __OpenID Connect__:
  [OpenID Connect 1.0](https://openid.net/specs/openid-connect-core-1_0.html) is a simple identity layer on top of the OAuth 2.0 protocol. It allows Clients to verify the identity of the End-User based on the authentication performed by an Authorization Server, as well as to obtain basic profile information about the End-User in an interoperable and REST-like manner.

<img src="images/federated_methods.png" width="500" height="800" alt="Federated identity methods and standard"/>

OAuth 2.0/2.1 and OpenID Connect will be described in more detail in the following sections. 

### OAuth 2.0/2.1 (OAuth2)

> The OAuth 2.1 authorization framework enables an application to obtain limited access to a protected resource, 
> either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and an
> authorization service, or by allowing the application to obtain access on its own behalf.
> 
> (*RFC 6749, OAuth 2.1 draft specification*)

#### Roles

OAuth 2.1 defines four roles:

* __Resource Owner__:  
An entity capable of granting access to a protected resource. When the resource owner is a person, it is referred to as an end-user.

* __Resource Server__:  
The server hosting the protected resources, capable of accepting and responding to protected resource requests using access tokens. The resource server is often accessible via an API (Rest, GraphQL etc.). Typical example here is backend service implemented in Java. 

* __Client__:  
An application making protected resource requests on behalf of the resource owner and with its authorization. The term _client_ does not imply any particular implementation characteristics (e.g., whether the application executes on a server, a desktop, in a web browser, or other devices).

* __Authorization Server__:  
The server issuing access tokens to the client after successfully authenticating the resource owner and obtaining authorization.

#### Access Tokens

Access tokens are credentials used to access protected resources.  An access token is a string representing an authorization issued to the client. The string is usually opaque to the client.  
Tokens  represent specific scopes and durations of access, granted by the resource owner, 
and enforced by the resource server and authorization server.
OAuth 2.0/2.1 does __not__ define any token format (it may be a opaque token or a JWT).

Access tokens are transmitted to the resource server as bearer tokens via the _authorization_ http header to authenticate the client at the resource server. The name bearer token implies that every bearer (holder) of the token is authenticated to retrieve protected resources from the resource server. The resource server cannot distinguish between a valid or malicious client presenting the token.

#### Refresh Tokens

![Opaque bearer token](images/oauth2_opaque_bearer_token.png)

#### Scopes

The authorization and token endpoints allow the client to specify the scope of the access request using the scope request parameter. In turn, the authorization server uses the scope response parameter to inform the client of the scope of the access token issued.

The value of the scope parameter is expressed as a list of space- delimited, case-sensitive strings. The strings are defined by the authorization server. If the value contains multiple space-delimited strings, their order does not matter, and each string adds an additional access range to the requested scope.

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
This variant was removed from the OAuth 2.1 spec because it contradicts and all concepts of OAuth 2.0/2.1 by insecurely exposing the credentials of the resource owner to the client.

![OAuth2 ro_password credentials grant + PKCE](images/oauth2_ro_password_credentials_flow.png)

##### Implicit Grant (removed in OAuth 2.1)  

The implicit grant historically has been used by single page applications running as javascript client in the web browser. As this grant type is causing the authorization server to issue access tokens in the authorization response it is vulnerable to access token leakage and access token replay. 

![OAuth2 implicit grant](images/oauth2_implicit_flow.png)

##### Client Credentials grant + PKCE

Client credentials are used as an authorization grant typically when the client is acting on its own behalf (the client is also the resource owner).
Typical clients are batch processing applications that run in a non-interactive mode without requiring a personal user account. This is comparable with authenticating using a technical user.

![OAuth2 client credentials grant + PKCE](images/oauth2_client_credentials_flow.png)

##### Authorization Code grant

![OAuth2 authorization code grant](images/oauth2_authz_code_flow.png)

##### Authorization Code grant + PKCE


![OAuth2 authorization code grant + PKCE](images/oauth2_authz_code_pkce_flow.png)


### OpenID Connect (OIDC) 1.0

#### Authorization Grants

#### ID and Access Tokens

![JWT bearer token](images/oauth2_jwt_bearer_token.png)
