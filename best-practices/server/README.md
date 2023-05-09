# Best Practices for the Server side

This section describes important security recommendations and best-practices for the server side.

## Main Risks on the server side

* Insufficient validation of tokens (Opaque tokens or JWT)
* Replay of stolen tokens on multiple resource servers

You always have to make sure to validate all tokens you get sent from clients to the server side before using these.
In case of opaque tokens you must validate these using the token introspection endpoint. In case of JWT you have to validate at least the signature and the expiry date/time.
In production systems you must use signed JSON Web Tokens (JWT) in all times. In test systems you may use unsigned tokens for to make testing easier but please make sure that these test tokens are really denied on production. In earlier times there have been some libraries that could be tricked to ignore the signature of a token if the header has been altered to signal an unsigned token.
Luckily today the libraries always deny such tokens that have a signature but have such a false header set.

To restrict tokens from being used everywhere it is also recommended to use the _audience_ claim to restrict a token for only some backend APIs. So, it a token gets stolen, the attack surface can be reduced by such an improvement.

## Best Practices and Recommendations

1. Always validate the access token on the server side (at least the signature and expiry date/time). It is recommended to check the _audience_ claim as well.
2. Keep lifetime short for access tokens (especially when these are stored in the browser), 5-30 minutes are OK
3. Always use a proven library for all that OAuth2 and OpenID Connect token validation stuff (you find a good library for almost every kind of programming language). Please check the [OpenID Connect certified implementations](https://openid.net/developers/certified/).

You can find even more recommendations and best-practices here at the [IETF](https://datatracker.ietf.org/wg/oauth/documents/):

* [OAuth 2.0 Security Best Current Practice](https://www.ietf.org/archive/id/draft-ietf-oauth-security-topics-22.html)
* [The OAuth 2.1 Authorization Framework](https://www.ietf.org/archive/id/draft-ietf-oauth-v2-1-08.html)
* [JSON Web Token Best Current Practices](https://www.rfc-editor.org/rfc/rfc8725.html)