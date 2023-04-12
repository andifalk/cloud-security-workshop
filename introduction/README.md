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
The vast majority of identity systems are implemented for the purpose of organizations and follow the administrative identity architecture type. The whole ruleset of operation, assigning identities, defining attributes, and sharing is determined by the organization.  

In the context of the internet users are required to have lots of different accounts. That is why user-centered identity systems have been established. By using protocols like OAuth 2.0 and OpenID Connect with social login (login with Facebook, Twitter, GitHub etc.) users are given a higher degree of autonomy. 

Modern self-sovereign identity systems give full control to participants in terms of certifiable identifiers, choice on sharing attributes and relationships.

![IAM phases](images/identity_system_architectures.png)

(Source: Learning Digital Identity)

## Federated Identities

<img src="images/federated_methods.png" width="500" height="800" alt="Federated identity methods and standard"/>


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

OAuth 2.0 originally defined more authorization grants than OAuth 2.1 does:

* __Resource Owner Password Grant__:  
The intention of this protocol variant was to make migration to OAuth 2.0 easier from other authentication mechanisms like basic authentication or form based authentication. Here still the client asks for user credentials and then sends these to the authorization server to get an access token.  
This variant was removed from the OAuth 2.1 spec because it contradicts and all concepts of OAuth 2.0/2.1 by insecurely exposing the credentials of the resource owner to the client.

* __Implicit Grant__:  
  The implicit grant historically has been used by single page applications running as javascript client in the web browser. As this grant type is causing the authorization server to issue access tokens in the authorization response it is vulnerable to access token leakage and access token replay. 

##### Client Credentials grant + PKCE

![OAuth2 client credentials grant + PKCE](images/oauth2_client_credentials_flow.png)

##### Authorization Code grant

![OAuth2 authorization code grant](images/oauth2_authz_code_flow.png)

##### Authorization Code grant + PKCE


![OAuth2 authorization code grant + PKCE](images/oauth2_authz_code_pkce_flow.png)



#### Tokens

![Opaque bearer token](images/oauth2_opaque_bearer_token.png)




#### Scopes

### OpenID Connect (OIDC) 1.0

#### Authorization Grants

#### Tokens

![JWT bearer token](images/oauth2_jwt_bearer_token.png)
